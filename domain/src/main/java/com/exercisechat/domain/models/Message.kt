package com.exercisechat.domain.models

import java.time.Instant

data class Message(
    val text: String,
    val senderUserId: Long,
    val receiverUserId: Long,
    val status: MessageStatus,
    val timestamp: Instant,
    val id: Long = 0
)

enum class MessageStatus {
    SENT,
    DELIVERED
}

