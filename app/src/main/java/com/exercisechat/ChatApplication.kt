package com.exercisechat

import android.app.Application
import com.exercisechat.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ChatApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ChatApplication)
            androidLogger()
            modules(appModule)
        }
    }
}
