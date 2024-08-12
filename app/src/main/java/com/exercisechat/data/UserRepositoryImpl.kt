package com.exercisechat.data

import com.exercisechat.data.source.local.UserDao
import com.exercisechat.domain.UserRepository
import com.exercisechat.domain.models.User
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun observeAll(): Flow<List<User>> {
        return userDao.getAll()
    }

    override suspend fun add(user: User): Long {
        return userDao.insert(user)
    }

    override suspend fun addAll(vararg users: User) {
        userDao.insertAll(*users)
    }

    override suspend fun get(userId: Long): User? {
        return userDao.get(userId)
    }
}
