package com.exercisechat.presentation.feature.users.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exercisechat.data.UserEntity
import com.exercisechat.presentation.feature.users.UsersContract

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun UsersTopBar(
    users: List<UserEntity>,
    onAction: (action: UsersContract.Event) -> Unit,
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
                    onAction(UsersContract.Event.GenerateNewUser)
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
                                    onAction(UsersContract.Event.ChangeActiveUser(it.id))
                                    showMenu = false
                                })
                        }
                    }
                }
            }
        )
    }
}
