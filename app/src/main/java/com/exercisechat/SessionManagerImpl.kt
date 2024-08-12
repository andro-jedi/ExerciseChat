package com.exercisechat

import android.content.Context
import com.exercisechat.domain.SessionManager
import com.exercisechat.domain.UserRepository
import com.exercisechat.domain.models.User

private const val CURRENT_USER_KEY = "current_user_id"

class SessionManagerImpl(
    context: Context,
    private val userRepository: UserRepository
) : SessionManager {

    private val sharedPref = context.getSharedPreferences("demo_prefs", Context.MODE_PRIVATE)

    override suspend fun getCurrentUser(): User? {
        val currentUserId = sharedPref.getLong(CURRENT_USER_KEY, 0)
        return userRepository.get(currentUserId)
    }

    override fun setCurrentUser(userId: Long) {
        sharedPref.edit().putLong(CURRENT_USER_KEY, userId).apply()
    }
}
