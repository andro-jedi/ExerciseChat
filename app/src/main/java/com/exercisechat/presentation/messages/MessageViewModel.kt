package com.exercisechat.presentation.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercisechat.domain.MessageRepository
import com.exercisechat.domain.SessionManager
import com.exercisechat.domain.UserRepository
import com.exercisechat.domain.models.Message
import com.exercisechat.domain.models.MessageStatus
import com.exercisechat.domain.models.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant

data class MessageUiState(
    val messages: List<Message>,
    val receiverUser: User? = null,
    val senderUser: User? = null
)

class MessageViewModel(
    private val receiverUserId: Long,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageUiState(emptyList()))
    val uiState: StateFlow<MessageUiState> = _uiState.asStateFlow()

    private lateinit var currentUser: User

    init {
        viewModelScope.launch {
            currentUser = sessionManager.getCurrentUser()!!

            userRepository.get(receiverUserId)?.let { user ->
                _uiState.update { it.copy(receiverUser = user, senderUser = currentUser) }
            }

            messageRepository.observeChat(currentUser.id, receiverUserId).collect { messages ->
                _uiState.update { it.copy(messages = messages) }
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            messageRepository.add(
                Message(
                    text = message,
                    currentUser.id,
                    receiverUserId,
                    MessageStatus.SENT,
                    Instant.now()
                )
            )
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            messageRepository.clearChat(currentUser.id, receiverUserId)
        }
    }
}
