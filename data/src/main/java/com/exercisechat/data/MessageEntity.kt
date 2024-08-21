package com.exercisechat.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.exercisechat.domain.models.MessageStatus
import java.time.Instant

@Entity(tableName = "message")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val senderUserId: Long,
    val receiverUserId: Long,
    val status: MessageStatus,
    val timestamp: Instant
)
