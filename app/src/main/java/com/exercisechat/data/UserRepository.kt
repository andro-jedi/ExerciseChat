package com.exercisechat.data

import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun observeAll(): Flow<List<User>>
    suspend fun add(vararg users: User)
    suspend fun get(userId: Int) : User?
}
