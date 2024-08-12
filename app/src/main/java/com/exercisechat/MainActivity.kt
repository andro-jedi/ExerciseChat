package com.exercisechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.exercisechat.data.User
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
        // fake current user session
        val currentUser by remember {
            mutableStateOf(User("Santa", "Bremor").apply { id = 99 })
        }
        NavHost(navController = navController, startDestination = Routes.USERS) {
            composable(route = Routes.USERS) {
                val viewModel: UsersViewModel = koinViewModel()
                val uiState = viewModel.uiState.collectAsState()
                UsersScreen(
                    users = uiState.value.users,
                    onUserClicked = { receiverUser ->
                        navController.navigate(
                            route = "chatScreen/${receiverUser.id}"
                        )
                    },
                    addNewUserClicked = {
                        viewModel.generateNewUser()
                    }
                )
            }

            composable(
                route = Routes.CHAT,
                arguments = listOf(navArgument("receiverUserId") { type = NavType.IntType })
            ) { backStackEntry ->
                val receiverUserId = backStackEntry.arguments?.getInt("receiverUserId")
                val viewModel: MessageViewModel = koinViewModel { parametersOf(currentUser.id, receiverUserId) }
                val uiState = viewModel.uiState.collectAsState()

                MessagesScreen(
                    navController = navController,
                    senderUser = currentUser,
                    receiverUser = uiState.value.receiverUser, // TODO
                    messages = uiState.value.messages,
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
