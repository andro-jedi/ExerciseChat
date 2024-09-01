package com.exercisechat.presentation.feature.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercisechat.data.UserMock
import com.exercisechat.data.toUserEntity
import com.exercisechat.domain.DispatchersProvider
import com.exercisechat.domain.SessionManager
import com.exercisechat.domain.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UsersViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsersContract.State(emptyList()))
    val uiState: StateFlow<UsersContract.State> = _uiState.asStateFlow()

    private val _uiEffect = Channel<UsersContract.Effect>(Channel.BUFFERED)
    val uiEffect: Flow<UsersContract.Effect> = _uiEffect.receiveAsFlow()

    init {
        viewModelScope.launch(dispatchersProvider.io) {
            userRepository.observeAll().combine(sessionManager.observeCurrentUserId()) { users, currentUserId ->
                users to currentUserId
            }.collect { (users, currentUserId) ->
                _uiState.update {
                    it.copy(
                        users = users.map { user -> user.toUserEntity() },
                        currentUserId = currentUserId
                    )
                }
            }
        }
    }

    fun handleEvent(action: UsersContract.Event) {
        when (action) {
            is UsersContract.Event.GenerateNewUser -> generateNewUser()
            is UsersContract.Event.ChangeActiveUser -> changeActiveUser(action.userId)
        }
    }

    /**
     * Generate random new user and add it to the database
     */
    private fun generateNewUser() {
        viewModelScope.launch(dispatchersProvider.io) {
            val id = userRepository.add(UserMock.newUser())
            // if current session is empty init it with first added user for demo purposes
            // it will be possible to change session user id later on users screen
            if (sessionManager.getCurrentUser() == null) {
               changeActiveUser(id)
            }
        }
    }

    private fun changeActiveUser(userId: Long) {
        viewModelScope.launch(dispatchersProvider.io) {
            sessionManager.setCurrentUser(userId)
            _uiState.update { it.copy(currentUserId = userId) }
            _uiEffect.send(UsersContract.Effect.UserSwitched)
        }
    }
}
