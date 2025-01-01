package com.trackit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavBar(
    onHomeClick: () -> Unit,
    onChartsClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    onExchangeClick: () -> Unit,
    onEditProfileClick: () -> Unit
) {
    BottomAppBar(
        containerColor = Color.White,
        tonalElevation = 4.dp
    ) {
        IconButton(onClick = onHomeClick, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home",
                tint = Color.Black
            )
        }

        IconButton(onClick = onChartsClick, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = Icons.Default.BarChart,
                contentDescription = "Charts",
                tint = Color.Black
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.Black)
                .align(Alignment.CenterVertically)
                .clickable { onAddButtonClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White
            )
        }

        IconButton(onClick = onExchangeClick, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = Icons.Default.CurrencyExchange,
                contentDescription = "Exchange",
                tint = Color.Black
            )
        }

        IconButton(onClick = onEditProfileClick, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Profile",
                tint = Color.Black
            )
        }
    }
}
