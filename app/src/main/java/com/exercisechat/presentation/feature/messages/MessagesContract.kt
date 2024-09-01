package com.exercisechat.presentation.feature.messages

import com.exercisechat.data.UserEntity
import com.exercisechat.domain.models.Message

class MessagesContract {

    data class State(
        val messages: List<Message>,
        val receiverUser: UserEntity? = null,
        val senderUser: UserEntity? = null
    )

    sealed class Event {
        data class SendMessage(val message: String) : Event()
        data object ClearChat : Event()
    }
}
