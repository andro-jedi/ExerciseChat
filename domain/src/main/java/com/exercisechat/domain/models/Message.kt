package com.exercisechat.domain.models

import java.time.Instant

data class Message(
    val text: String,
    val senderUserId: Long,
    val receiverUserId: Long,
    val status: MessageStatus,
    val timestamp: Instant,
    val id: Long = 0,
    val messageSpacing: MessageSpacing = MessageSpacing.DEFAULT,
    val needsHeader: Boolean = false
)

enum class MessageStatus {
    SENT,
    DELIVERED
}

enum class MessageSpacing {
    DEFAULT,
    LARGE
}

