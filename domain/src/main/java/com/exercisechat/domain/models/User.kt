package com.exercisechat.domain.models

data class User(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val avatarId: Int = 1
)
