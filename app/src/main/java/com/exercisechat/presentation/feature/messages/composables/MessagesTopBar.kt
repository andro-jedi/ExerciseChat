package com.exercisechat.presentation.feature.messages.composables

import androidx.annotation.IntRange
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.exercisechat.R
import com.exercisechat.data.UserEntity
import com.exercisechat.presentation.feature.messages.MessagesContract

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun MessagesTopBar(
    receiverUser: UserEntity?,
    navController: NavHostController,
    onAction: (action: MessagesContract.Event) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    TopAppBar(
        modifier = Modifier
            .shadow(4.dp)
            .background(MaterialTheme.colorScheme.primary),
        title = {
            Row(modifier = Modifier.wrapContentWidth(), verticalAlignment = Alignment.CenterVertically) {
                if (receiverUser != null) {
                    Image(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = getUserAvatar(receiverUser.avatarId)),
                        contentDescription = "avatar"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = receiverUser.firstName,
                        fontSize = 18.sp
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigateUp()
            }, content = {
                Image(
                    painter = painterResource(id = R.drawable.arrow_back_ios_24),
                    contentDescription = "avatar"
                )
            })
        },
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(painter = painterResource(id = R.drawable.baseline_more_horiz_24), contentDescription = "More")
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Clear history") },
                    onClick = {
                        onAction(MessagesContract.Event.ClearChat)
                        showMenu = false
                    }
                )
            }
        }
    )
}

private fun getUserAvatar(@IntRange(1, 3) avatarId: Int): Int {
    return when (avatarId) {
        1 -> R.drawable.avatar1
        2 -> R.drawable.avatar2
        else -> R.drawable.avatar3
    }
}
