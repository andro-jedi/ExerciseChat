package com.exercisechat.di

import com.exercisechat.SessionManagerImpl
import com.exercisechat.data.di.databaseModule
import com.exercisechat.domain.DispatchersProvider
import com.exercisechat.domain.SessionManager
import com.exercisechat.utils.DefaultDispatchersProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    includes(
        databaseModule,
        repositoryModule,
        viewModelModule
    )

    factoryOf(::SessionManagerImpl) bind SessionManager::class

    singleOf(::DefaultDispatchersProvider) bind DispatchersProvider::class
}
