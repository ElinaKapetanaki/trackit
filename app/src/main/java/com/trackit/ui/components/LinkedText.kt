package com.trackit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LinkedText(
    staticText: String,
    clickableText: String,
    onClick: () -> Unit,
    staticTextStyle: TextStyle = TextStyle(color = Color.Gray, fontSize = 14.sp),
    linkTextStyle: TextStyle = TextStyle(color = Color.Blue, fontSize = 14.sp, textDecoration = TextDecoration.Underline)
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = staticText,
            style = staticTextStyle,
            modifier = Modifier
        )

        TextButton(
            onClick = onClick,
            contentPadding = PaddingValues(0.dp) // Remove padding for seamless appearance
        ) {
            Text(
                text = clickableText,
                style = linkTextStyle
            )
        }
    }
}
