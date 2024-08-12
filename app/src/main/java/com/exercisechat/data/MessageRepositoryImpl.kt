package com.exercisechat.data

import com.exercisechat.data.source.local.MessageDao
import com.exercisechat.data.source.local.UserDao
import kotlinx.coroutines.flow.Flow

class MessageRepositoryImpl(
    private val userDao: UserDao,
    private val messageDao: MessageDao
) : MessageRepository {

    override suspend fun observeChat(senderUserId: Int, receiverUserId: Int): Flow<List<Message>> {
        return messageDao.getAllSorted(senderUserId, receiverUserId)
    }

    override suspend fun add(message: Message) {
        messageDao.insert(message)
    }
}
