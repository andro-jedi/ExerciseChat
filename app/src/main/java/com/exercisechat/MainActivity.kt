package com.exercisechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.exercisechat.ui.messages.MessagesScreen
import com.exercisechat.ui.theme.ExerciseChatTheme
import com.exercisechat.ui.users.UsersScreen
import com.exercisechat.ui.users.UsersViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExerciseChatTheme {
                MainContent()
            }
        }
    }

    @Composable
    private fun MainContent() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Routes.USERS) {
            composable(route = Routes.USERS) {
                val viewModel: UsersViewModel = koinViewModel()
                val uiState = viewModel.uiState.collectAsState()
                UsersScreen(
                    users = uiState.value.users,
                    onUserClicked = {
                        navController.navigate(Routes.CHAT)
                    },
                    addNewUserClicked = {
                        viewModel.generateNewUser()
                    }
                )
            }

            composable(route = Routes.CHAT) {
                MessagesScreen(navController)
            }
        }
    }
}

object Routes {
    const val USERS = "usersScreen"
    const val CHAT = "chatScreen"
}
