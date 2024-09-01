package com.exercisechat.presentation.feature.messages.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exercisechat.presentation.theme.Colors
import com.exercisechat.presentation.theme.ExerciseChatTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val timestampFormatter = DateTimeFormatter.ofPattern("EEEE HH:mm").withZone(ZoneId.systemDefault())

@Composable
internal fun MessagesHeader(timestamp: Instant) {
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

@Preview
@Composable
private fun MessagesTimestampPreview() {
    ExerciseChatTheme {
        MessagesHeader(timestamp = Instant.now())
    }
}
