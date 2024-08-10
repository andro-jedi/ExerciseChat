package com.exercisechat.data

import androidx.room.Entity
import androidx.room.PrimaryKey
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

