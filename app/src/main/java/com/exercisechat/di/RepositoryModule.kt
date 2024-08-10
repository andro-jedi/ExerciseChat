package com.exercisechat.di

import com.exercisechat.data.UserRepository
import com.exercisechat.data.UserRepositoryImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    single { UserRepositoryImpl(get()) } bind UserRepository::class
}
