package com.exercisechat.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE id == :userId")
    suspend fun get(userId: Long): User?

    @Insert
    suspend fun insert(vararg users: User)

    @Delete
    suspend fun delete(user: User)
}
