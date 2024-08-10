package com.exercisechat.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM message WHERE userId IN (:userIds)")
    fun getAll(userIds: List<Long>): Flow<List<Message>>

    @Insert
    suspend fun insert(vararg messages: Message)

    @Delete
    suspend fun delete(message: Message)
}
