package com.exercisechat.data.source.local

import androidx.room.*
import com.exercisechat.data.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user WHERE id == :userId")
    suspend fun get(userId: Long): UserEntity?

    @Insert
    suspend fun insert(user: UserEntity): Long

    @Transaction
    @Insert
    suspend fun insertAll(vararg users: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)
}
