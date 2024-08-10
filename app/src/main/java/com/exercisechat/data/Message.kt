package com.exercisechat.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.Instant

@Entity
data class Message(
    @PrimaryKey val id: Long,
    val text: String,
    val userId: Int,
    val status: MessageStatus,
    val timestamp: Instant
)

enum class MessageStatus {
    SENT,
    DELIVERED,
    READ
}

class MessageConverters {

    @TypeConverter
    fun fromMessageStatus(status: MessageStatus): String {
        return status.name
    }

    @TypeConverter
    fun toMessageStatus(status: String): MessageStatus {
        return MessageStatus.valueOf(status)
    }

    @TypeConverter
    fun fromMessageInstant(time: Instant): Long {
        return time.epochSecond
    }

    @TypeConverter
    fun toMessageInstant(seconds: Long): Instant {
        return Instant.ofEpochSecond(seconds)
    }
}
