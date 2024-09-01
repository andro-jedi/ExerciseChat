package com.exercisechat.presentation.feature.users.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exercisechat.R
import com.exercisechat.data.UserEntity
import com.exercisechat.presentation.theme.Colors

@Composable
internal fun UserItem(
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
