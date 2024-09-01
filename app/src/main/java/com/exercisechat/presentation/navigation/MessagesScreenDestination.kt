package com.exercisechat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.exercisechat.presentation.feature.messages.MessageViewModel
import com.exercisechat.presentation.feature.messages.composables.MessagesScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MessagesScreenDestination(receiverUserId: Long?, navController: NavHostController) {
    val viewModel: MessageViewModel = koinViewModel { parametersOf(receiverUserId) }
    val uiState by viewModel.uiState.collectAsState()

    MessagesScreen(
        navController = navController,
        senderUser = uiState.senderUser,
        receiverUser = uiState.receiverUser,
        messages = uiState.messages,
        onEvent = viewModel::handleEvent
    )
}
