package com.exercisechat.data

import kotlinx.coroutines.flow.Flow

interface UserRepository {

    /**
     * Observe all users
     *
     * @return flow of all users
     */
    suspend fun observeAll(): Flow<List<User>>

    /**
     * Add multiple new users
     */
    suspend fun addAll(vararg users: User)

    /**
     * Add new user
     *
     * @return id of new user
     */
    suspend fun add(user: User): Long

    /**
     * Get user by id
     *
     * @return user or null if not found
     */
    suspend fun get(userId: Long) : User?
}
