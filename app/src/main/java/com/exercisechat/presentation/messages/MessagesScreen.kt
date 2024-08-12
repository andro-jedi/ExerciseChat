package com.exercisechat.presentation.messages

import androidx.annotation.IntRange
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.exercisechat.R
import com.exercisechat.domain.models.Message
import com.exercisechat.domain.models.MessageStatus
import com.exercisechat.domain.models.User
import com.exercisechat.ui.theme.Colors
import com.exercisechat.ui.theme.ExerciseChatTheme
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private const val SMALL_MESSAGE_SPACING = 4
private const val LARGE_MESSAGE_SPACING = 12
private val applyLargeSpacingAfter = Duration.ofSeconds(20)

private val timestampFormatter = DateTimeFormatter.ofPattern("EEEE HH:mm").withZone(ZoneId.systemDefault())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    navController: NavHostController,
    senderUser: User?,
    receiverUser: User?,
    messages: List<Message>,
    onSendMessage: (message: String) -> Unit,
    onChatCleared: () -> Unit
) {
    Scaffold(
        topBar = {
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
                                onChatCleared()
                                showMenu = false
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (messages.isEmpty()) {
                EmptyChat()
            } else {
                if (senderUser != null) {
                    MessagesColumn(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxSize()
                            .weight(1f),
                        messages = messages,
                        senderUser = senderUser,
                    )
                } else {
                    LoadingChat(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    )
                }
            }
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
fun LoadingChat(modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
        )
    }
}

@Composable
fun ColumnScope.EmptyChat() {
    Text(
        modifier = Modifier
            .wrapContentSize()
            .weight(1f),
        color = Colors.DarkGrey,
        text = stringResource(id = R.string.text_empty_chat),
        fontSize = 24.sp
    )
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
            maxLines = 5,
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
private fun MessagesColumn(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    senderUser: User
) {
    /**
     * Returns the spacing between the current message and the previous message.
     *
     * @param previousMessage the previous message, or null if this is the first message
     * @param currentMessage the current message
     * @return large or small spacing
     */
    fun getMessageSpacing(previousMessage: Message?, currentMessage: Message): Dp {
        if (previousMessage == null) return SMALL_MESSAGE_SPACING.dp

        return if (previousMessage.senderUserId == currentMessage.senderUserId
            && Duration.between(
                previousMessage.timestamp,
                currentMessage.timestamp
            ).seconds < applyLargeSpacingAfter.seconds
        ) {
            SMALL_MESSAGE_SPACING.dp
        } else {
            LARGE_MESSAGE_SPACING.dp
        }
    }

    LazyColumn(
        modifier = modifier,
        reverseLayout = true
    ) {
        itemsIndexed(messages) { index, message ->
            // access the previous item if it exists
            // since array is sorted by descending we are using next index, not previous
            val previousMessage = messages.getOrNull(index + 1)

            val messageSpacing = getMessageSpacing(previousMessage, message)

            val isCurrentUser = message.senderUserId == senderUser.id
            // prevent the bubble to take all width of the screen to make UI more readable
            val bubblePadding = if (isCurrentUser) {
                Modifier.padding(start = 48.dp)
            } else {
                Modifier.padding(end = 48.dp)
            }
            MessageBubble(
                modifier = Modifier
                    .padding(top = messageSpacing)
                    .then(bubblePadding),
                text = message.text,
                status = MessageStatus.SENT,
                isCurrentUser = isCurrentUser
            )

            // display timestamp if a previous message was sent more than an hour ago, or there is no previous messages
            if (previousMessage == null) {
                MessageTimestamp(message.timestamp)
            } else {
                val messageAge = Duration.between(previousMessage.timestamp, message.timestamp)
                if (messageAge.toHours() > 1) {
                    MessageTimestamp(message.timestamp)
                }
            }
        }
    }
}

@Composable
private fun MessageTimestamp(timestamp: Instant) {
    val (day, time) = timestampFormatter.format(timestamp).split(" ")

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            modifier = Modifier
                .wrapContentWidth(),
            text = day,
            color = Colors.DarkGrey,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier
                .wrapContentWidth(),
            text = time,
            color = Colors.DarkGrey
        )
    }
}

@Composable
private fun MessageBubble(
    text: String,
    status: MessageStatus,
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isCurrentUser) Colors.BubblePink else Colors.LightGrey,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomEnd = if (isCurrentUser) 0.dp else 16.dp,
                        bottomStart = if (isCurrentUser) 16.dp else 0.dp
                    )
                )
        ) {
            BasicText(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(16.dp),
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = if (isCurrentUser) Color.White else Color.DarkGray,
                    fontSize = 16.sp
                )
            )
            if (isCurrentUser) {
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
                    senderUserId = 2,
                    receiverUserId = 1,
                    status = MessageStatus.SENT,
                    Instant.ofEpochSecond(123456789)
                )
            ),
            onSendMessage = {},
            onChatCleared = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MessagesEmptyChatPreview() {
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
            messages = emptyList(),
            onSendMessage = {},
            onChatCleared = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MessagesLoadingChatPreview() {
    ExerciseChatTheme {
        MessagesScreen(
            navController = rememberNavController(),
            receiverUser = null,
            senderUser = null,
            messages = listOf(
                Message(
                    text = "Hello World!",
                    senderUserId = 2,
                    receiverUserId = 1,
                    status = MessageStatus.SENT,
                    Instant.ofEpochSecond(123456789)
                )
            ),
            onSendMessage = {},
            onChatCleared = {}
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

@Preview
@Composable
private fun MessagesTimestampPreview() {
    ExerciseChatTheme {
        MessageTimestamp(timestamp = Instant.now())
    }
}
