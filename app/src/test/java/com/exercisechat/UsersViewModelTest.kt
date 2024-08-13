package com.exercisechat

import app.cash.turbine.test
import com.exercisechat.data.toUserEntity
import com.exercisechat.domain.SessionManager
import com.exercisechat.domain.UserRepository
import com.exercisechat.domain.models.User
import com.exercisechat.presentation.users.UsersUiState
import com.exercisechat.presentation.users.UsersViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest : CoroutineTest() {

    private val userRepository: UserRepository = mock()

    private val sessionManager: SessionManager = mock()

    private fun provideViewModel(): UsersViewModel {
        return UsersViewModel(userRepository, sessionManager, dispatchers)
    }

    @Test
    fun `generateNewUser adds user and updates session if empty`() = runTest {
        val newUser = User(1, "Test", "User")

        whenever(userRepository.add(any())).thenReturn(newUser.id)
        whenever(sessionManager.getCurrentUser()).thenReturn(null).thenReturn(newUser) // First null, then the user
        wheneverBlocking { sessionManager.observeCurrentUserId() }.thenReturn(flowOf(newUser.id))
        wheneverBlocking { userRepository.observeAll() }.thenReturn(flowOf(listOf(newUser)))

        val viewModel = provideViewModel()
        viewModel.generateNewUser()
        advanceUntilIdle()

        verify(userRepository).add(any())
        verify(sessionManager).setCurrentUser(newUser.id)
    }

    @Test
    fun `changeActiveUser updates session and uiState`() = runTest {
        val currentUser = User(1, "Test", "User")

        whenever(sessionManager.getCurrentUser()).thenReturn(null).thenReturn(currentUser) // First null, then the user
        wheneverBlocking { sessionManager.observeCurrentUserId() }.thenReturn(flowOf(currentUser.id))
        wheneverBlocking { userRepository.observeAll() }.thenReturn(flowOf(listOf(currentUser)))

        val userId = 1L
        val viewModel = provideViewModel()
        viewModel.changeActiveUser(userId)
        advanceUntilIdle()

        verify(sessionManager).setCurrentUser(userId)
        viewModel.uiState.test {
            assertEquals(userId, awaitItem().currentUserId)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState is updated with users and currentUserId`() = runTest {
        val users = listOf(
            User(1, "Test1", "User1"),
            User(2, "Test2", "User2")
        )
        val currentUserId = 1L
        whenever(userRepository.observeAll()).thenReturn(flowOf(users))
        whenever(sessionManager.observeCurrentUserId()).thenReturn(flowOf(currentUserId))
        val viewModel = provideViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            assertEquals(
                UsersUiState(users.map { it.toUserEntity() }, currentUserId),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
}
