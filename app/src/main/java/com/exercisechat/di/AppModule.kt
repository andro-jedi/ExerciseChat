package com.exercisechat.di

import org.koin.dsl.module

val appModule = module {
    includes(databaseModule, repositoryModule)
}
