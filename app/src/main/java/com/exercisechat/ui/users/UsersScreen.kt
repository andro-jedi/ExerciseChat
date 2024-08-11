package com.exercisechat.ui.users

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exercisechat.data.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    users: List<User>,
    onUserClicked: (user: User) -> Unit,
    addNewUserClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            Box(
                Modifier
                    .shadow(4.dp)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Contacts",
                            fontSize = 24.sp
                        )
                    },
                    actions = {
                        IconButton(onClick = addNewUserClicked) {
                            Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "Add user")
                        }
                    }
                )
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                items(users, key = { user -> user.id }) { user ->
                    UserItem(user, onUserClicked)
                }
            }
        }
    )
}

@Composable
fun UserItem(user: User, onUserClicked: (user: User) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(80.dp)
            .clickable { onUserClicked(user) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            text = user.fullName,
            fontSize = 32.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun UsersScreenPreview() {
    MaterialTheme {
        UsersScreen(
            users = listOf(User("Lucash", "Bobkin")),
            onUserClicked = {},
            addNewUserClicked = {}
        )
    }
}
