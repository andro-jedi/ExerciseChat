package com.exercisechat.data

import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    /**
     * Observe a chat between two users
     */
    suspend fun observeChat(senderUserId: Int, receiverUserId: Int): Flow<List<Message>>

    /**
     * Add new message to the chat
     */
    suspend fun add(message: Message)

    /**
     * Clear chat between two users
     */
    suspend fun clearChat(currentUserId: Int, receiverUserId: Int)
}
