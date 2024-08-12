package com.exercisechat.domain

import com.exercisechat.domain.models.User
import kotlinx.coroutines.flow.Flow

/**
 * Session manager interface to hold active user session
 */
interface SessionManager {

    suspend fun getCurrentUser(): User?

    fun setCurrentUser(userId: Long)

    fun observeCurrentUserId(): Flow<Long>
}
