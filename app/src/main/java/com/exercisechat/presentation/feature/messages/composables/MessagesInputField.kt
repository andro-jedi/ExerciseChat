package com.exercisechat.presentation.feature.messages.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exercisechat.R
import com.exercisechat.presentation.theme.Colors

@Composable
fun MessagesInputField(
    modifier: Modifier = Modifier,
    onSendMessage: (String) -> Unit
) {
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    var isFocused by remember { mutableStateOf(false) }

    /**
     * Sends the message and clears the input field.
     */
    fun sendMessage() {
        onSendMessage(textState.text)
        textState = TextFieldValue("")
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
