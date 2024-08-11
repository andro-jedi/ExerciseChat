package com.exercisechat.ui.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercisechat.data.Message
import com.exercisechat.data.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MessageUiState(
    val receiverUserId: Int,
    val messages: List<Message>
)

class MessageViewModel(
    private val currentUserId: Int,
    private val receiverUserId: Int,
    private val messageRepository: MessageRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageUiState(receiverUserId, emptyList()))
    val uiState: StateFlow<MessageUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
//            messageRepository.observeAll().collect { users ->
//                _uiState.update { it.copy(users = users) }
//            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
//            messageRepository.add(
//                Message(
//                    text = message,
//
//                )
//            )
        }
    }
}
