package com.exercisechat.data

import androidx.annotation.IntRange
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val id: Int,
    val firstName: String,
    val lastName: String,
    @IntRange(1-10) val avatarId: Int = 1
)
