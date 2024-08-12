package com.exercisechat.data

import androidx.annotation.IntRange
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    val firstName: String,
    val lastName: String,
    @IntRange(1, 3) val avatarId: Int = 1
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    val fullName: String
        get() = "$firstName $lastName"
}
