package com.exercisechat.domain

import com.exercisechat.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    /**
     * Observe a chat between two users
     */
    suspend fun observeChat(senderUserId: Long, receiverUserId: Long): Flow<List<Message>>

    /**
     * Add new message to the chat
     */
    suspend fun add(message: Message)

    /**
     * Clear chat between two users
     */
    suspend fun clearChat(currentUserId: Long, receiverUserId: Long)
}
