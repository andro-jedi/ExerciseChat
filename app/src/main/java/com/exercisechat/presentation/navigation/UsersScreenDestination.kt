package com.exercisechat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.exercisechat.presentation.feature.users.UsersViewModel
import com.exercisechat.presentation.feature.users.composables.UsersScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun UsersScreenDestination(navController: NavHostController) {
    val viewModel: UsersViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    UsersScreen(
        users = uiState.users,
        currentUserId = uiState.currentUserId,
        effectFlow = viewModel.uiEffect,
        onUserClicked = { receiverUser ->
            navController.navigate(
                route = "chatScreen/${receiverUser.id}"
            )
        },
        onEvent = viewModel::handleEvent
    )
}
