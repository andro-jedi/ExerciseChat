package com.exercisechat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.exercisechat.presentation.navigation.Navigation.Messages
import com.exercisechat.presentation.navigation.Navigation.Users
import kotlinx.serialization.Serializable

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Users) {
        composable<Users> {
            UsersScreenDestination(navController)
        }

        composable<Messages> { backStackEntry ->
            val messages = backStackEntry.toRoute<Messages>()
            MessagesScreenDestination(messages.receiverUserId, navController)
        }
    }
}

object Navigation {

    @Serializable
    object Users

    @Serializable
    data class Messages(val receiverUserId: Long)
}
