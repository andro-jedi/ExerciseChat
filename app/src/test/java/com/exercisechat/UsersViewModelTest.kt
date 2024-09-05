package com.exercisechat

import app.cash.turbine.test
import com.exercisechat.data.toUserEntity
import com.exercisechat.domain.SessionManager
import com.exercisechat.domain.UserRepository
import com.exercisechat.domain.models.User
import com.exercisechat.presentation.feature.users.UsersContract
import com.exercisechat.presentation.feature.users.UsersViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest : CoroutineTest() {

    private val userRepository: UserRepository = mockk(relaxed = true)

    private val sessionManager: SessionManager = mockk(relaxed = true)

    private lateinit var viewModel: UsersViewModel

    @Before
    fun setup() {
        viewModel = UsersViewModel(userRepository, sessionManager, dispatchers)
    }

    @Test
    fun `generateNewUser adds user and updates session if empty`() = runTest {
        val userId = 1L

        coEvery { userRepository.add(any()) } returns userId
        coEvery { sessionManager.getCurrentUser()} returns null
        coEvery { sessionManager.setCurrentUser(userId) } just runs

        viewModel.handleEvent(UsersContract.Event.GenerateNewUser)
        advanceUntilIdle()

        coVerify { userRepository.add(any()) }
        coVerify { sessionManager.setCurrentUser(userId) }
    }

    @Test
    fun `changeActiveUser updates session and uiState`() = runTest {
        val currentUser = User(1, "Test", "User")
        val newUser = User(2, "Test2", "User2")

        coEvery { sessionManager.getCurrentUser() } returns currentUser
        coEvery { sessionManager.observeCurrentUserId() } returns flowOf(currentUser.id)
        coEvery { userRepository.observeAll() } returns flowOf(listOf(currentUser))

        viewModel.handleEvent(UsersContract.Event.ChangeActiveUser(newUser.id))
        advanceUntilIdle()

        coVerify { sessionManager.setCurrentUser(newUser.id) }
        viewModel.uiState.test {
            assertEquals(newUser.id, awaitItem().currentUserId)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState is updated with users and currentUserId`() = runTest {
        val currentUserId = 1L
        val users = listOf(
            User(id = currentUserId, firstName = "Test1", lastName = "User1"),
            User(id = 2, firstName = "Test2", lastName = "User2")
        )
        coEvery { userRepository.observeAll() } returns flowOf(users)
        coEvery { sessionManager.observeCurrentUserId() } returns flowOf(currentUserId)

        viewModel = UsersViewModel(userRepository, sessionManager, dispatchers)
        advanceUntilIdle()

        viewModel.uiState.test {
            val expected = UsersContract.State(users.map { it.toUserEntity() }, currentUserId)
            assertEquals(
                expected,
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
}
