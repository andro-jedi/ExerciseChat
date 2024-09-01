package com.exercisechat.presentation.feature.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercisechat.data.UserEntity
import com.exercisechat.data.toUserEntity
import com.exercisechat.domain.*
import com.exercisechat.domain.models.Message
import com.exercisechat.domain.models.MessageSpacing
import com.exercisechat.domain.models.MessageStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.time.measureDurationForResult
import java.time.Duration
import java.time.Instant
import kotlin.time.Duration.Companion.seconds

private val applyLargeSpacingAfter = Duration.ofSeconds(20)

class MessageViewModel(
    private val receiverUserId: Long,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessagesContract.State(emptyList()))
    val uiState: StateFlow<MessagesContract.State> = _uiState.asStateFlow()

    private lateinit var currentUser: UserEntity

    init {
        viewModelScope.launch(dispatchersProvider.io) {
            // can't be null at this point for demo purposes
            currentUser = sessionManager.getCurrentUser()!!.toUserEntity()

            userRepository.get(receiverUserId)?.let { user ->
                _uiState.update { it.copy(receiverUser = user.toUserEntity(), senderUser = currentUser) }
            }

            messageRepository.observeChat(currentUser.id, receiverUserId).collect { messages ->
                var previousMessage: Message? = null
                val uiMessages = measureDurationForResult {
                    messages.map { message ->
                        message.copy(messageSpacing = getMessageSpacing(previousMessage, message))
                            .also { previousMessage = message }
                    }.reversed()
                }
                _uiState.update { it.copy(messages = uiMessages.first) }
            }
        }
    }

    fun handleEvent(action: MessagesContract.Event) {
        when (action) {
            is MessagesContract.Event.SendMessage -> sendMessage(action.message)
            MessagesContract.Event.ClearChat -> clearChat()
        }
    }

    /**
     * Returns the spacing between the current message and the previous message.
     *
     * @param previousMessage the previous message, or null if this is the first message
     * @param currentMessage the current message
     * @return large or small spacing
     */
    private fun getMessageSpacing(previousMessage: Message?, currentMessage: Message): MessageSpacing {
        if (previousMessage == null) return MessageSpacing.DEFAULT

        return if (previousMessage.senderUserId == currentMessage.senderUserId
            && Duration.between(
                previousMessage.timestamp,
                currentMessage.timestamp
            ).seconds < applyLargeSpacingAfter.seconds
        ) {
            MessageSpacing.DEFAULT
        } else {
            MessageSpacing.LARGE
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
