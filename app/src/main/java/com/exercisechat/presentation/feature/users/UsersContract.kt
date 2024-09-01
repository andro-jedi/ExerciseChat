package com.exercisechat.presentation.feature.users

import com.exercisechat.data.UserEntity

class UsersContract {
    data class State(
        val users: List<UserEntity>,
        val currentUserId: Long = 0
    )

    sealed class Event {
        data object GenerateNewUser : Event()
        data class ChangeActiveUser(val userId: Long) : Event()
    }

    sealed interface Effect {
        data object UserSwitched : Effect
    }
}
