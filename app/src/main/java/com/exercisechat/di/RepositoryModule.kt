package com.exercisechat.di

import com.exercisechat.data.MessageRepositoryImpl
import com.exercisechat.data.UserRepositoryImpl
import com.exercisechat.domain.MessageRepository
import com.exercisechat.domain.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {

    singleOf(::UserRepositoryImpl) bind UserRepository::class

    singleOf(::MessageRepositoryImpl) bind MessageRepository::class
}
