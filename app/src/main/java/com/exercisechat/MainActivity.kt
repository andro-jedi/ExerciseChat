package com.exercisechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import com.exercisechat.ui.theme.ExerciseChatTheme
import com.exercisechat.ui.users.UsersScreen
import com.exercisechat.ui.users.UsersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val usersViewModel: UsersViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExerciseChatTheme {
                val usersUiState = usersViewModel.uiState.collectAsState()
                UsersScreen(
                    users = usersUiState.value.users,
                    onUserClicked = {

                    },
                    addNewUserClicked = {

                    }
                )
            }
        }
    }
}
