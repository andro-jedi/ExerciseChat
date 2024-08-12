package com.exercisechat.di

import com.exercisechat.domain.SessionManager
import com.exercisechat.domain.SessionManagerImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    includes(
        databaseModule,
        repositoryModule,
        viewModelModule
    )

    factoryOf(::SessionManagerImpl) bind SessionManager::class
}
