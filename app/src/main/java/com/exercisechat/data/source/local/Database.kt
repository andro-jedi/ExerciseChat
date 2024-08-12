package com.exercisechat.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.exercisechat.domain.models.Message
import com.exercisechat.domain.models.User

@TypeConverters(MessageConverters::class)
@Database(entities = [User::class, Message::class], version = 1)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
}
