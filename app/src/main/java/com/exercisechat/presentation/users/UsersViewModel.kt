package com.exercisechat.presentation.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercisechat.data.UserEntity
import com.exercisechat.data.UserMock
import com.exercisechat.data.toUserEntity
import com.exercisechat.domain.DispatchersProvider
import com.exercisechat.domain.SessionManager
import com.exercisechat.domain.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UsersUiState(
    val users: List<UserEntity>,
    val currentUserId: Long = 0
)

class UsersViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsersUiState(emptyList()))
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(dispatchersProvider.io) {
            userRepository.observeAll().combine(sessionManager.observeCurrentUserId()) { users, currentUserId ->
                users to currentUserId
            }.collect { (users, currentUserId) ->
                _uiState.update { it.copy(users = users.map { it.toUserEntity() }, currentUserId = currentUserId) }
            }
        }
    }

    /**
     * Generate random new user and add it to the database
     */
    fun generateNewUser() {
        viewModelScope.launch(dispatchersProvider.io) {
            val id = userRepository.add(UserMock.newUser())
            // if current session is empty init it with first added user for demo purposes
            // it will be possible to change session user id later on users screen
            if (sessionManager.getCurrentUser() == null) {
               changeActiveUser(id)
            }
        }
    }

    fun changeActiveUser(userId: Long) {
        viewModelScope.launch(dispatchersProvider.io) {
            sessionManager.setCurrentUser(userId)

            _uiState.update { it.copy(currentUserId = userId) }
        }
    }
}
