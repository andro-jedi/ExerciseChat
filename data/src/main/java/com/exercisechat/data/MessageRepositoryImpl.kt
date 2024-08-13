package com.exercisechat.data

import com.exercisechat.data.source.local.MessageDao
import com.exercisechat.domain.MessageRepository
import com.exercisechat.domain.models.Message
import com.exercisechat.domain.models.MessageStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageRepositoryImpl(
    private val messageDao: MessageDao
) : MessageRepository {

    override suspend fun observeChat(senderUserId: Long, receiverUserId: Long): Flow<List<Message>> {
        return messageDao.getAllSorted(senderUserId, receiverUserId).map { it.map { it.toMessageDomain() } }
    }

    override suspend fun add(message: Message): Long {
        return messageDao.insert(message.toMessageEntity())
    }

    override suspend fun updateStatus(messageId: Long, status: MessageStatus) {
        messageDao.updateStatus(messageId, status)
    }

    override suspend fun clearChat(currentUserId: Long, receiverUserId: Long) {
        messageDao.deleteAllMessagesBetween(currentUserId, receiverUserId)
    }

    override suspend fun get(id: Long): Message? {
        return messageDao.get(id)?.toMessageDomain()
    }

    override suspend fun updateMessage(message: Message) {
        return messageDao.update(message.toMessageEntity())
    }
}
