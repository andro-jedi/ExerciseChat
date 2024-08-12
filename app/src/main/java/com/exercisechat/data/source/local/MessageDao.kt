package com.exercisechat.data.source.local

import androidx.room.*
import com.exercisechat.data.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM message WHERE senderUserId == :senderUserId AND receiverUserId == :receiverUserId ORDER BY id DESC")
    fun getAllSorted(senderUserId: Int, receiverUserId: Int): Flow<List<Message>>

    @Insert
    suspend fun insert(vararg messages: Message)

    @Delete
    suspend fun delete(message: Message)
}
