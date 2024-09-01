package com.exercisechat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Navigation.Routes.USERS) {
        composable(route = Navigation.Routes.USERS) {
            UsersScreenDestination(navController)
        }

        composable(
            route = Navigation.Routes.CHAT,
            arguments = listOf(navArgument(Navigation.Args.RECEIVER_USER_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            val receiverUserId = backStackEntry.arguments?.getLong(Navigation.Args.RECEIVER_USER_ID)
            MessagesScreenDestination(receiverUserId, navController)
        }
    }
}

object Navigation {
    object Args {

        const val RECEIVER_USER_ID = "receiverUserId"
    }

    object Routes {

        const val USERS = "usersScreen"
        const val CHAT = "chatScreen/{receiverUserId}"
    }
}
