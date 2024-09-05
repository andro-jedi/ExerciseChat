package com.exercisechat

import com.exercisechat.data.UserEntity
import com.exercisechat.data.toUserDomain
import com.exercisechat.domain.MessageRepository
import com.exercisechat.domain.SessionManager
import com.exercisechat.domain.UserRepository
import com.exercisechat.domain.models.Message
import com.exercisechat.domain.models.MessageSpacing
import com.exercisechat.domain.models.MessageStatus
import com.exercisechat.presentation.feature.messages.MessageViewModel
import com.exercisechat.presentation.feature.messages.MessagesContract
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class MessageViewModelTest : CoroutineTest() {

    private val currentUser = UserEntity(1, "Current", "User", 1)
    private val currentUserDomain = currentUser.toUserDomain()

    private val sessionManager: SessionManager = mockk {
        coEvery { getCurrentUser() } returns currentUserDomain
    }

    private val messageRepository: MessageRepository = mockk(relaxed = true) {
        coEvery { observeChat(any(), any()) } returns flowOf()
    }

    private val userRepository: UserRepository = mockk(relaxed = true) {
        coEvery { observeAll() } returns flowOf(listOf(currentUserDomain))
    }

    private fun provideViewModel(receiverUserId: Long = 2L): MessageViewModel {
        return MessageViewModel(
            receiverUserId,
            messageRepository,
            userRepository,
            sessionManager,
            dispatchers
        )
    }

    @Test
    fun `sendMessage adds message to repository`() = runTest {
        val messageId = 3L
        val receiverUserId = 2L
        val text = "Test"

        coEvery { messageRepository.add(any()) } returns messageId

        val viewModel = provideViewModel(receiverUserId)
        viewModel.handleEvent(MessagesContract.Event.SendMessage(text))

        advanceUntilIdle()

        coVerify { messageRepository.add(any()) }
        coVerify { messageRepository.get(messageId) }
    }

    @Test
    fun `clearChat calls repository to clear chat`() = runTest {
        val receiverUserId = 2L

        val viewModel = provideViewModel()
        viewModel.handleEvent(MessagesContract.Event.ClearChat)

        advanceUntilIdle()

        coVerify { messageRepository.clearChat(currentUser.id, receiverUserId) }
    }

    @Test
    fun `uiState is updated with messages and users when initiated`() = runTest {
        val receiverUserId = 2L
        val receiverUser = UserEntity(receiverUserId, "Receiver", "User")
        val timestamp = Instant.now()
        val messages = listOf(
            Message(
                text = "Hi",
                senderUserId = currentUser.id,
                receiverUserId = receiverUserId,
                status = MessageStatus.SENT,
                timestamp = timestamp,
                needsHeader = true
            ),
            Message(
                text = "Hello",
                senderUserId = receiverUserId,
                receiverUserId = currentUser.id,
                status = MessageStatus.SENT,
                timestamp = timestamp,
                messageSpacing = MessageSpacing.LARGE
            )
        )
        coEvery { sessionManager.getCurrentUser() } returns currentUserDomain
        coEvery { userRepository.get(any()) } returns receiverUser.toUserDomain()
        coEvery { messageRepository.observeChat(any(), any()) } returns flowOf(messages)

        val viewModel = provideViewModel()
        advanceUntilIdle()

        assertEquals(
            MessagesContract.State(messages.reversed(), receiverUser, currentUser),
            viewModel.uiState.value
        )
    }
}
