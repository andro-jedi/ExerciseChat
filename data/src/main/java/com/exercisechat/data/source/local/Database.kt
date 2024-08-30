package com.exercisechat.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.exercisechat.data.MessageEntity
import com.exercisechat.data.UserEntity

@TypeConverters(MessageConverters::class)
@Database(entities = [UserEntity::class, MessageEntity::class], version = 1, exportSchema = false)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
}
