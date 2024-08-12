package com.exercisechat.data

import com.exercisechat.data.source.local.UserDao
import com.exercisechat.domain.UserRepository
import com.exercisechat.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun observeAll(): Flow<List<User>> {
        return userDao.getAll().map { it.map { it.toUserDomain() } }
    }

    override suspend fun add(user: User): Long {
        return userDao.insert(user.toUserEntity())
    }

    override suspend fun addAll(vararg users: User) {
        userDao.insertAll(*users.map { it.toUserEntity() }.toTypedArray())
    }

    override suspend fun get(userId: Long): User? {
        return userDao.get(userId)?.toUserDomain()
    }
}
