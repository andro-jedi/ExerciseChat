package com.exercisechat

import android.content.Context
import android.content.SharedPreferences
import com.exercisechat.domain.SessionManager
import com.exercisechat.domain.UserRepository
import com.exercisechat.domain.models.User
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

private const val CURRENT_USER_KEY = "current_user_id"

/**
 * Session manager interface to hold active user session
 */
class SessionManagerImpl(
    context: Context,
    private val userRepository: UserRepository
) : SessionManager {

    private val sharedPref = context.getSharedPreferences("demo_prefs", Context.MODE_PRIVATE)

    override suspend fun getCurrentUser(): User? {
        val currentUserId = sharedPref.getLong(CURRENT_USER_KEY, 0)
        return userRepository.get(currentUserId)
    }

    override fun observeCurrentUserId(): Flow<Long> = callbackFlow {
        fun ProducerScope<Long>.emitValue(prefs: SharedPreferences) {
            launch { send(prefs.getLong(CURRENT_USER_KEY, 0)) }
        }

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, changedKey ->
            if (changedKey == CURRENT_USER_KEY) emitValue(prefs)
        }
        sharedPref.registerOnSharedPreferenceChangeListener(listener)

        // Emit the initial value
        emitValue(sharedPref)

        awaitClose { sharedPref.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override fun setCurrentUser(userId: Long) {
        sharedPref.edit().putLong(CURRENT_USER_KEY, userId).apply()
    }
}
