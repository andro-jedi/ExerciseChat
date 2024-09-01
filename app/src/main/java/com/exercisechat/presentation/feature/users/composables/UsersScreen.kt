package com.exercisechat.presentation.feature.users.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.exercisechat.data.UserEntity
import com.exercisechat.presentation.feature.users.UsersContract

@Composable
fun UsersScreen(
    users: List<UserEntity>,
    currentUserId: Long,
    onUserClicked: (user: UserEntity) -> Unit,
    onEvent: (action: UsersContract.Event) -> Unit
) {
    Scaffold(
        topBar = {
            UsersTopBar(users, onEvent)
        },
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
            onUserClicked = {},
            onEvent = {}
        )
    }
}
