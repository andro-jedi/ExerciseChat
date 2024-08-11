package com.exercisechat.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercisechat.data.User
import com.exercisechat.data.UserMock
import com.exercisechat.data.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UsersUiState(
    val users: List<User>
)

class UsersViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsersUiState(emptyList()))
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.observeAll().collect { users ->
                _uiState.update { it.copy(users = users) }
            }
        }
    }

    fun generateNewUser() {
        viewModelScope.launch {
            userRepository.add(UserMock.newUser())
        }
    }
}
