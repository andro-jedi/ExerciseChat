package com.exercisechat.data

import com.exercisechat.data.source.local.MessageDao
import kotlinx.coroutines.flow.Flow

class MessageRepositoryImpl(
    private val messageDao: MessageDao
) : MessageRepository {

    override suspend fun observeChat(senderUserId: Long, receiverUserId: Long): Flow<List<Message>> {
        return messageDao.getAllSorted(senderUserId, receiverUserId)
    }

    override suspend fun add(message: Message) {
        messageDao.insert(message)
    }

    override suspend fun clearChat(currentUserId: Long, receiverUserId: Long) {
        messageDao.deleteAllMessagesBetween(currentUserId, receiverUserId)
    }
}
