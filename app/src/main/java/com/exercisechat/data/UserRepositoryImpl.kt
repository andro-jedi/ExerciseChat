package com.exercisechat.data

import com.exercisechat.data.source.local.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun observeAll(): Flow<List<User>> {
        return userDao.getAll()
    }

    override suspend fun add(user: User) {
        userDao.insert(user)
    }
}
