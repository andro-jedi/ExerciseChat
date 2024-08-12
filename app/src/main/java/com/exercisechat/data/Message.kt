package com.exercisechat.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "message")
data class Message(
    val text: String,
    val senderUserId: Long,
    val receiverUserId: Long,
    val status: MessageStatus,
    val timestamp: Instant
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

enum class MessageStatus {
    SENT,
    DELIVERED
}

