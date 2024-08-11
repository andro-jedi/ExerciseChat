package com.exercisechat.di

import com.exercisechat.data.UserRepository
import com.exercisechat.data.UserRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::UserRepositoryImpl) bind UserRepository::class
}
