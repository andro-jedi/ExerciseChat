package com.exercisechat.presentation.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercisechat.data.UserEntity
import com.exercisechat.data.toUserEntity
import com.exercisechat.domain.*
import com.exercisechat.domain.models.Message
import com.exercisechat.domain.models.MessageStatus
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant

data class MessageUiState(
    val messages: List<Message>,
    val receiverUser: UserEntity? = null,
    val senderUser: UserEntity? = null
)

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
        }
        viewModelScope.launch(dispatchersProvider.io) {
            messageRepository.observeChat(currentUser.id, receiverUserId).collect { messages ->
                _uiState.update { it.copy(messages = messages) }
            }
        }
    }

    fun sendMessage(message: String, timestamp: Instant = Instant.now()) {
        viewModelScope.launch(dispatchersProvider.io) {
            messageRepository.add(
                Message(
                    text = message,
                    currentUser.id,
                    receiverUserId,
                    MessageStatus.SENT,
                    timestamp
                )
            )
        }
    }

    fun clearChat() {
        viewModelScope.launch(dispatchersProvider.io) {
            messageRepository.clearChat(currentUser.id, receiverUserId)
        }
    }
}
