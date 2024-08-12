package com.exercisechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.exercisechat.ui.messages.MessageViewModel
import com.exercisechat.ui.messages.MessagesScreen
import com.exercisechat.ui.theme.ExerciseChatTheme
import com.exercisechat.ui.users.UsersScreen
import com.exercisechat.ui.users.UsersViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

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
                val uiState by viewModel.uiState.collectAsState()
                UsersScreen(
                    users = uiState.users,
                    currentUserId = uiState.currentUserId,
                    onUserClicked = { receiverUser ->
                        navController.navigate(
                            route = "chatScreen/${receiverUser.id}"
                        )
                    },
                    addNewUserClicked = {
                        viewModel.generateNewUser()
                    },
                    changeActiveUserClicked = { user ->
                        viewModel.changeActiveUser(user.id)
                    }
                )
            }

            composable(
                route = Routes.CHAT,
                arguments = listOf(navArgument("receiverUserId") { type = NavType.LongType })
            ) { backStackEntry ->
                val receiverUserId = backStackEntry.arguments?.getLong("receiverUserId")
                val viewModel: MessageViewModel = koinViewModel { parametersOf(receiverUserId) }
                val uiState by viewModel.uiState.collectAsState()

                MessagesScreen(
                    navController = navController,
                    senderUser = uiState.senderUser,
                    receiverUser = uiState.receiverUser,
                    messages = uiState.messages,
                    onSendMessage = { message ->
                        viewModel.sendMessage(message)
                    },
                    onChatCleared = {
                        viewModel.clearChat()
                    }
                )
            }
        }
    }
}

object Routes {

    const val USERS = "usersScreen"
    const val CHAT = "chatScreen/{receiverUserId}"
}
