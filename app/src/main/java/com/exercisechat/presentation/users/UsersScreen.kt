package com.exercisechat.presentation.users

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exercisechat.R
import com.exercisechat.data.UserEntity
import com.exercisechat.ui.theme.Colors

@Composable
fun UsersScreen(
    users: List<UserEntity>,
    currentUserId: Long,
    onUserClicked: (user: UserEntity) -> Unit,
    onAction: (action: UsersUiAction) -> Unit
) {
    Scaffold(
        topBar = {
            UsersListTopBar(users, onAction)
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun UsersListTopBar(
    users: List<UserEntity>,
    onAction: (action: UsersUiAction) -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }
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
                IconButton(onClick = {
                    onAction(UsersUiAction.GenerateNewUser)
                }) {
                    Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "Add user")
                }
                if (users.size > 1) {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Change user"
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        users.forEach {
                            DropdownMenuItem(
                                text = { Text(it.fullName) },
                                onClick = {
                                   onAction(UsersUiAction.ChangeActiveUser(it.id))
                                    showMenu = false
                                })
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun UserItem(
    user: UserEntity,
    isCurrentUser: Boolean,
    onUserClicked: (user: UserEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth()
            .clickable(enabled = !isCurrentUser) { onUserClicked(user) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .wrapContentWidth(),
            text = user.fullName,
            fontSize = 32.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (isCurrentUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier
                    .wrapContentSize(),
                text = "(${stringResource(id = R.string.text_your)})",
                fontSize = 24.sp,
                color = Colors.DarkGrey
            )
        }
    }
}

@Preview
@Composable
private fun UsersScreenPreview() {
    MaterialTheme {
        UsersScreen(
            users = listOf(UserEntity(0, "Lucash", "Bobkin")),
            currentUserId = 1,
            onUserClicked = {},
            onAction = {}
        )
    }
}
