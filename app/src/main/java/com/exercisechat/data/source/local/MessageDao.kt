package com.exercisechat.data.source.local

import androidx.room.*
import com.exercisechat.data.Message
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
    fun getAllSorted(senderUserId: Long, receiverUserId: Long): Flow<List<Message>>

    @Insert
    suspend fun insert(vararg messages: Message)

    @Delete
    suspend fun delete(message: Message)

    @Transaction
    @Query(
        """
        DELETE FROM message WHERE (senderUserId == :senderId AND receiverUserId == :receiverId) 
        OR (senderUserId == :receiverId AND receiverUserId == :senderId)
        """
    )
    suspend fun deleteAllMessagesBetween(senderId: Long, receiverId: Long)
}
