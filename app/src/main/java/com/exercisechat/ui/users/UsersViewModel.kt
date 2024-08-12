package com.exercisechat.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercisechat.data.User
import com.exercisechat.data.UserMock
import com.exercisechat.data.UserRepository
import com.exercisechat.domain.SessionManagerImpl
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UsersUiState(
    val users: List<User>,
    val currentUserId: Long = 0
)

class UsersViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManagerImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsersUiState(emptyList()))
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.observeAll().collect { users ->
                val currentUser = sessionManager.getCurrentUser()
                _uiState.update { it.copy(users = users, currentUserId = currentUser?.id ?: 0) }
            }
        }
    }

    /**
     * Generate random new user and add it to the database
     */
    fun generateNewUser() {
        viewModelScope.launch {
            val id = userRepository.add(UserMock.newUser())
            // if current session is empty init it with first added user for demo purposes
            // it will be possible to change session user id later on users screen
            if (sessionManager.getCurrentUser() == null) {
               changeActiveUser(id)
            }
        }
    }

    fun changeActiveUser(userId: Long) {
        viewModelScope.launch {
            sessionManager.setCurrentUser(userId)

            _uiState.update { it.copy(currentUserId = userId) }
        }
    }
}
