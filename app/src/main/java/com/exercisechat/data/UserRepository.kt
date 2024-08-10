package com.exercisechat.data

import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun observeAll(): Flow<List<User>>
    suspend fun add(user: User)
}
