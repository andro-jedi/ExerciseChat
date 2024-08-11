package com.exercisechat.data

import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun observeChat(senderUserId: Int, receiverUserId: Int): Flow<List<Message>>
    suspend fun add(message: Message)
}
