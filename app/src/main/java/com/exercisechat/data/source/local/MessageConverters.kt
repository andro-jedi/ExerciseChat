package com.exercisechat.data.source.local

import androidx.room.TypeConverter
import com.exercisechat.data.MessageStatus
import java.time.Instant

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
