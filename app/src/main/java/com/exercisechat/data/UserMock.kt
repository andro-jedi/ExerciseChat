package com.exercisechat.data

object UserMock {

    private val firstNames = listOf("Adam", "John", "Alex", "Kiril", "Sasha", "Dom")
    private val lastNames = listOf("Bobkin", "Smith", "Truth", "Green", "Jackson", "Dom")

    fun newUser() = User(
        firstName = firstNames.random(),
        lastName = lastNames.random(),
        avatarId = (1..3).random()
    )
}
