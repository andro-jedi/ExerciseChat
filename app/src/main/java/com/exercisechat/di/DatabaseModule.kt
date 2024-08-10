package com.exercisechat.di

import androidx.room.Room
import com.exercisechat.ChatApplication
import com.exercisechat.data.source.local.ChatDatabase
import org.koin.dsl.module

fun provideDataBase(application: ChatApplication): ChatDatabase =
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
