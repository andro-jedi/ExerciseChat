package com.exercisechat.data

import com.exercisechat.domain.models.Message
import com.exercisechat.domain.models.User

// UserEntity Mappers
fun UserEntity.toUserDomain(): User {
    return User(
        id = id,
        firstName = firstName,
        lastName = lastName,
        avatarId = avatarId
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        firstName = firstName,
        lastName = lastName,
        avatarId = avatarId
    ).apply { id = this@toUserEntity.id }
}

// Message Mappers
fun MessageEntity.toMessageDomain(): Message {
    return Message(
        id = id,
        text = text,
        senderUserId = senderUserId,
        receiverUserId = receiverUserId,
        status = status,
        timestamp = timestamp
    )
}

fun Message.toMessageEntity(): MessageEntity {
    return MessageEntity(
        text = text,
        senderUserId = senderUserId,
        receiverUserId = receiverUserId,
        status = status,
        timestamp = timestamp
    ).apply { id = this@toMessageEntity.id }
}
