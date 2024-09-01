package com.exercisechat.presentation.feature.messages.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exercisechat.domain.models.MessageStatus
import com.exercisechat.presentation.theme.Colors
import com.exercisechat.presentation.theme.ExerciseChatTheme

@Composable
internal fun MessagesBubble(
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
                    imageVector = Icons.Filled.Check,
                    contentDescription = "message status",
                    colorFilter = ColorFilter.tint(statusColor)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(4.dp))
}

@Preview
@Composable
private fun MessagesBubblesPreview() {
    ExerciseChatTheme {
        Column {
            MessagesBubble(text = "Demo1", status = MessageStatus.SENT, isCurrentUser = true)
            MessagesBubble(text = "Demo2Demo2Demo2", status = MessageStatus.DELIVERED, isCurrentUser = false)
        }
    }
}
