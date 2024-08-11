package com.exercisechat.ui.messages

import androidx.annotation.IntRange
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.exercisechat.R
import com.exercisechat.data.User
import com.exercisechat.ui.theme.ExerciseChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    navController: NavHostController,
    opponent: User
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .shadow(4.dp)
                    .background(MaterialTheme.colorScheme.primary),
                title = {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            modifier = Modifier.size(48.dp),
                            painter = painterResource(id = getUserAvatar(opponent.avatarId)),
                            contentDescription = "avatar"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = opponent.firstName,
                            fontSize = 18.sp
                        )
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
                    Image(
                        painter = painterResource(id = R.drawable.baseline_more_horiz_24),
                        contentDescription = "avatar"
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(text = "Demo")
        }
    }
}

private fun getUserAvatar(@IntRange(1, 3) avatarId: Int): Int {
    return when (avatarId) {
        1 -> R.drawable.avatar1
        2 -> R.drawable.avatar2
        else -> R.drawable.avatar3
    }
}

@Preview
@Composable
private fun MessagesScreenPreview() {
    ExerciseChatTheme {
        MessagesScreen(
            navController = rememberNavController(),
            opponent = User(
                firstName = "First",
                lastName = "Last",
                avatarId = 2
            )
        )
    }
}
