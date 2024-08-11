package com.exercisechat.ui.messages

import androidx.annotation.IntRange
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.exercisechat.R
import com.exercisechat.data.Message
import com.exercisechat.data.MessageStatus
import com.exercisechat.data.User
import com.exercisechat.ui.theme.Colors
import com.exercisechat.ui.theme.ExerciseChatTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    navController: NavHostController,
    senderUser: User,
    receiverUser: User,
    messages: List<Message>,
    onSendMessage: (message: String) -> Unit
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
                            painter = painterResource(id = getUserAvatar(receiverUser.avatarId)),
                            contentDescription = "avatar"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = receiverUser.firstName,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MessagesColumn(
                modifier = Modifier.padding(12.dp),
                messages = messages,
                senderUser = senderUser,
            )
            Spacer(
                modifier = Modifier
                    .height(4.dp)
                    .fillMaxWidth()
                    .rotate(180f)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                DefaultShadowColor.copy(alpha = 0.1f),
                                Color.Transparent,
                            )
                        )
                    )
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                MessageInputField(
                    modifier = Modifier.padding(12.dp),
                    onSendMessage = onSendMessage
                )
            }
        }
    }
}

@Composable
fun MessageInputField(
    modifier: Modifier = Modifier,
    onSendMessage: (String) -> Unit
) {
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    var isFocused by remember { mutableStateOf(false) }

    fun sendMessage() {
        onSendMessage(textState.text)
        textState = TextFieldValue("") // Clear the input field after sending
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .imePadding(), // make sure we have normal padding when keyboard is visible
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = textState,
            onValueChange = { textState = it },
            modifier = Modifier
                .weight(1f)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    border = BorderStroke(1.dp, if (isFocused) Color.Blue else Color.Gray),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = { sendMessage() }
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = { sendMessage() },
            enabled = textState.text.isNotEmpty(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Colors.BubblePink.copy(alpha = if (textState.text.isNotEmpty()) 1f else 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_send_24),
                    contentDescription = "send message",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.MessagesColumn(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    senderUser: User
) {
    var previousTimestamp: Instant? by remember { mutableStateOf(null) }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .weight(1f),
        reverseLayout = true
    ) {
        items(messages) { message ->
            val messageTime = message.timestamp
            val formatter = DateTimeFormatter.ofPattern("EEE HH:mm").withZone(ZoneId.systemDefault())
            val currentDateTime = formatter.format(messageTime)
            val previousDateTime = previousTimestamp?.let { formatter.format(it) }

            if (previousTimestamp == null || previousDateTime != currentDateTime) {
                Text(
                    text = currentDateTime,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Light,
                        color = Color.Gray
                    ),
                    textAlign = TextAlign.Center
                )
            }

            previousTimestamp = messageTime

            MessageBubble(
                text = message.text,
                status = MessageStatus.SENT,
                isCurrentUser = message.senderUserId == senderUser.id
            )
        }
    }
}

@Composable
private fun MessageBubble(
    text: String,
    status: MessageStatus,
    isCurrentUser: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = if (isCurrentUser) Arrangement.Start else Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isCurrentUser) Colors.LightGrey else Colors.BubblePink,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomEnd = if (isCurrentUser) 16.dp else 0.dp,
                        bottomStart = if (isCurrentUser) 0.dp else 16.dp
                    )
                )
                .widthIn(max = 250.dp)
        ) {
            BasicText(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(16.dp),
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = if (isCurrentUser) Color.DarkGray else Color.White,
                    fontSize = 16.sp
                )
            )
            if (!isCurrentUser) {
                val statusColor = when (status) {
                    MessageStatus.SENT -> Colors.StatusSent
                    MessageStatus.DELIVERED -> Colors.StatusDelivered
                }
                Image(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(16.dp)
                        .align(Alignment.BottomEnd),
                    painter = painterResource(id = R.drawable.checkmark),
                    contentDescription = "message status",
                    colorFilter = ColorFilter.tint(statusColor)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(4.dp))
}

private fun getUserAvatar(@IntRange(1, 3) avatarId: Int): Int {
    return when (avatarId) {
        1 -> R.drawable.avatar1
        2 -> R.drawable.avatar2
        else -> R.drawable.avatar3
    }
}

@Preview(showBackground = true)
@Composable
private fun MessagesScreenPreview() {
    ExerciseChatTheme {
        MessagesScreen(
            navController = rememberNavController(),
            receiverUser = User(
                firstName = "First",
                lastName = "Last",
                avatarId = 2
            ).apply { id = 1 },
            senderUser = User(
                firstName = "Second",
                lastName = "Last",
                avatarId = 3
            ).apply { id = 2 },
            messages = listOf(
                Message(
                    text = "Hello World!",
                    senderUserId = 1,
                    receiverUserId = 2,
                    status = MessageStatus.SENT,
                    Instant.ofEpochSecond(123456789)
                )
            ),
            onSendMessage = {}
        )
    }
}

@Preview
@Composable
private fun MessagesBubblesPreview() {
    ExerciseChatTheme {
        Column {
            MessageBubble(text = "Demo1", status = MessageStatus.SENT, isCurrentUser = true)
            MessageBubble(text = "Demo2Demo2Demo2", status = MessageStatus.DELIVERED, isCurrentUser = false)
        }
    }
}

@Preview
@Composable
private fun MessagesInputPreview() {
    ExerciseChatTheme {
        MessageInputField {

        }
    }
}
