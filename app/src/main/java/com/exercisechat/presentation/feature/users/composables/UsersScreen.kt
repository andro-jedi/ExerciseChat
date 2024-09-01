package com.exercisechat.presentation.feature.users.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.exercisechat.R
import com.exercisechat.data.UserEntity
import com.exercisechat.presentation.feature.users.UsersContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun UsersScreen(
    users: List<UserEntity>,
    currentUserId: Long,
    effectFlow: Flow<UsersContract.Effect>,
    onUserClicked: (user: UserEntity) -> Unit,
    onEvent: (action: UsersContract.Event) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when (effect) {
                UsersContract.Effect.UserSwitched -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.current_user_switched)
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = { UsersTopBar(users, onEvent) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                items(users, key = { user -> user.id }) { user ->
                    UserItem(
                        user = user,
                        isCurrentUser = user.id == currentUserId,
                        onUserClicked = onUserClicked
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun UsersScreenPreview() {
    MaterialTheme {
        UsersScreen(
            users = listOf(UserEntity(0, "Lucash", "Bobkin")),
            currentUserId = 1,
            effectFlow = emptyFlow(),
            onUserClicked = {},
            onEvent = {}
        )
    }
}
