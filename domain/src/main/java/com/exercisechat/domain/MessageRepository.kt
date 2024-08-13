package com.exercisechat.domain

import com.exercisechat.domain.models.Message
import com.exercisechat.domain.models.MessageStatus
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    /**
     * Observe a chat between two users
     */
    suspend fun observeChat(senderUserId: Long, receiverUserId: Long): Flow<List<Message>>

    /**
     * Add new message to the chat
     */
    suspend fun add(message: Message): Long

    /**
     * Get message by id
     */
    suspend fun get(id: Long): Message?

    /**
     * Update message
     */
    suspend fun updateMessage(message: Message)

    /**
     * Update message status
     */
    suspend fun updateStatus(messageId: Long, status: MessageStatus)

    /**
     * Clear chat between two users
     */
    suspend fun clearChat(currentUserId: Long, receiverUserId: Long)
}
