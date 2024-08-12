package com.exercisechat.domain

import com.exercisechat.data.User

/**
 * Session manager interface to hold active user session
 */
interface SessionManager {

    suspend fun getCurrentUser(): User?

    fun setCurrentUser(userId: Long)
}
