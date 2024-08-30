package com.exercisechat.presentation.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercisechat.data.UserEntity
import com.exercisechat.data.toUserEntity
import com.exercisechat.domain.*
import com.exercisechat.domain.models.Message
import com.exercisechat.domain.models.MessageStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import kotlin.time.Duration.Companion.seconds

data class MessageUiState(
    val messages: List<Message>,
    val receiverUser: UserEntity? = null,
    val senderUser: UserEntity? = null
)

sealed class MessageUiAction {
    data class SendMessage(val message: String) : MessageUiAction()
    data object ClearChat : MessageUiAction()
}

class MessageViewModel(
    private val receiverUserId: Long,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageUiState(emptyList()))
    val uiState: StateFlow<MessageUiState> = _uiState.asStateFlow()

    private lateinit var currentUser: UserEntity

    init {
        viewModelScope.launch(dispatchersProvider.io) {
            // can't be null at this point for demo purposes
            currentUser = sessionManager.getCurrentUser()!!.toUserEntity()

            userRepository.get(receiverUserId)?.let { user ->
                _uiState.update { it.copy(receiverUser = user.toUserEntity(), senderUser = currentUser) }
            }

            messageRepository.observeChat(currentUser.id, receiverUserId).collect { messages ->
                _uiState.update { it.copy(messages = messages) }
            }
        }
    }

    fun onAction(action: MessageUiAction) {
        when (action) {
            is MessageUiAction.SendMessage -> sendMessage(action.message)
            MessageUiAction.ClearChat -> clearChat()
        }
    }

    private fun sendMessage(message: String, timestamp: Instant = Instant.now()) {
        viewModelScope.launch(dispatchersProvider.io) {
            val messageId = messageRepository.add(
                Message(
                    text = message,
                    currentUser.id,
                    receiverUserId,
                    MessageStatus.SENT,
                    timestamp
                )
            )

            launch {
                // simulate some delay for demo purposes
                delay((1..3).random().seconds)
                messageRepository.get(messageId)?.copy(status = MessageStatus.DELIVERED)?.let {
                    messageRepository.updateMessage(it)
                }
            }
        }
    }

    private fun clearChat() {
        viewModelScope.launch(dispatchersProvider.io) {
            messageRepository.clearChat(currentUser.id, receiverUserId)
        }
    }
}
