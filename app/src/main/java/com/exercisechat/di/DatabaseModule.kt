package com.exercisechat.di

import android.app.Application
import androidx.room.Room
import com.exercisechat.data.source.local.ChatDatabase
import org.koin.dsl.module

fun provideDataBase(application: Application): ChatDatabase =
    Room.databaseBuilder(
        application,
        ChatDatabase::class.java,
        "chat_db"
    )
        .fallbackToDestructiveMigration().build()

val databaseModule = module {
    single { provideDataBase(get()) }

    single { get<ChatDatabase>().userDao() }
    single { get<ChatDatabase>().messageDao() }
}
