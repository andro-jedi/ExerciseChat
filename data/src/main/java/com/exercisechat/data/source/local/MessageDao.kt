package com.exercisechat.data.source.local

import androidx.room.*
import com.exercisechat.data.MessageEntity
import com.exercisechat.domain.models.MessageStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query(
        """
        SELECT * FROM message 
        WHERE (senderUserId == :senderUserId AND receiverUserId == :receiverUserId) 
        OR (senderUserId == :receiverUserId AND receiverUserId == :senderUserId) 
        ORDER BY id DESC
        """
    )
    fun getAllSorted(senderUserId: Long, receiverUserId: Long): Flow<List<MessageEntity>>

    @Insert
    suspend fun insert(message: MessageEntity): Long

    @Delete
    suspend fun delete(message: MessageEntity)

    @Query("SELECT * FROM message WHERE id = :messageId")
    suspend fun get(messageId: Long): MessageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(message: MessageEntity)

    @Transaction
    @Query(
        """
        DELETE FROM message WHERE (senderUserId == :senderId AND receiverUserId == :receiverId) 
        OR (senderUserId == :receiverId AND receiverUserId == :senderId)
        """
    )
    suspend fun deleteAllMessagesBetween(senderId: Long, receiverId: Long)

    @Query("UPDATE message SET status = :status WHERE id = :messageId")
    suspend fun updateStatus(messageId: Long, status: MessageStatus)
}
