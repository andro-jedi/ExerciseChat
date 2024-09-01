package com.exercisechat

import com.exercisechat.data.UserEntity
import com.exercisechat.data.toUserDomain
import com.exercisechat.domain.MessageRepository
import com.exercisechat.domain.SessionManager
import com.exercisechat.domain.UserRepository
import com.exercisechat.domain.models.Message
import com.exercisechat.domain.models.MessageStatus
import com.exercisechat.domain.models.User
import com.exercisechat.presentation.feature.messages.MessageUiAction
import com.exercisechat.presentation.feature.messages.MessageUiState
import com.exercisechat.presentation.feature.messages.MessageViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.*
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class MessageViewModelTest : CoroutineTest() {

    private val currentUser = UserEntity(1, "Current", "User", 1)
    private val currentUserDomain = currentUser.toUserDomain()

    private val sessionManager: SessionManager = mock {
        onBlocking { getCurrentUser() } doReturn currentUserDomain
    }

    private val messageRepository: MessageRepository = mock {
        onBlocking { observeChat(any(), any()) } doReturn flowOf()
    }

    private val userRepository: UserRepository = mock {
        onBlocking { observeAll() } doReturn flowOf(listOf(currentUserDomain))
    }

    private fun provideViewModel(): MessageViewModel {
        return MessageViewModel(
            2L,
            messageRepository,
            userRepository,
            sessionManager, dispatchers
        )
    }

    @Test
    fun `sendMessage adds message to repository`() = runTest {
        val messageText = "Hello"
        whenever(userRepository.get(any())).thenReturn(User(2, "Receiver", "User"))
        whenever(messageRepository.add(any())).thenReturn(1)
        val viewModel = provideViewModel()
        viewModel.onAction(MessageUiAction.SendMessage(messageText))

        advanceUntilIdle()

        verify(messageRepository).add(
            argThat { message ->
                assertEquals(messageText, message.text)
                assertEquals(currentUser.id, message.senderUserId)
                assertEquals(2L, message.receiverUserId) // Use the correct receiver ID
                true // Return true to indicate a match
            }
        )
    }

    @Test
    fun `clearChat calls repository to clear chat`() = runTest {
        val viewModel = provideViewModel()
        viewModel.onAction(MessageUiAction.ClearChat)

        advanceUntilIdle()

        verify(messageRepository).clearChat(currentUser.id, 2L)
    }

    @Test
    fun `uiState is updated with messages and users`() = runTest {
        val receiverUser = UserEntity(2, "Receiver", "User")
        val messages = listOf(
            Message("Hi", currentUser.id, 2, MessageStatus.SENT, Instant.now()),
            Message("Hello", 2, currentUser.id, MessageStatus.SENT, Instant.now())
        )
        whenever(sessionManager.getCurrentUser()).thenReturn(currentUserDomain)
        whenever(userRepository.get(any())).thenReturn(User(2, "Receiver", "User"))
        whenever(messageRepository.observeChat(any(), any())).thenReturn(flowOf(messages))

        val viewModel = provideViewModel()

        advanceUntilIdle()

        assertEquals(
            MessageUiState(messages, receiverUser, currentUser),
            viewModel.uiState.value
        )
    }
}
