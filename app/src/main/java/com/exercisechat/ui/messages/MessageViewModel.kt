package com.exercisechat.ui.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercisechat.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant

data class MessageUiState(
    val messages: List<Message>,
    val receiverUser: User? = null
)

class MessageViewModel(
    private val currentUserId: Int,
    private val receiverUserId: Int,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageUiState(emptyList()))
    val uiState: StateFlow<MessageUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.get(receiverUserId)?.let { user ->
                _uiState.update { it.copy(receiverUser = user) }
            }
        }

        viewModelScope.launch {
            messageRepository.observeChat(currentUserId, receiverUserId).collect { messages ->
                _uiState.update { it.copy(messages = messages) }
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            messageRepository.add(
                Message(
                    text = message,
                    currentUserId,
                    receiverUserId,
                    MessageStatus.SENT,
                    Instant.now()
                )
            )
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            messageRepository.clearChat(currentUserId, receiverUserId)
        }
    }
}
