package com.exercisechat.di

import com.exercisechat.data.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {

    singleOf(::UserRepositoryImpl) bind UserRepository::class

    singleOf(::MessageRepositoryImpl) bind MessageRepository::class
}
