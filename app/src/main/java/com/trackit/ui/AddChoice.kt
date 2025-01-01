package com.trackit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trackit.ui.components.BlackButton
import com.trackit.ui.components.PageTopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddChoiceScreen(
                    onAddExpenseClick: () -> Unit,
                    onAddIncomeClick: () -> Unit,
                    onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            PageTopBar(
                title = "Choose Action",
                onBackClick = onBackClick
            )

        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF5F5F5))
                    .padding(16.dp)
                    .padding(top= 210.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "What would you like to do?",
                        style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    BlackButton(
                        text = "Add Expense",
                        onClick = onAddExpenseClick,
                        cornerRadius = 20
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    BlackButton(
                        text = "Add Income",
                        onClick = onAddIncomeClick,
                        cornerRadius = 20
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AddChoiceScreenPreview() {
    AddChoiceScreen(
        onAddExpenseClick = {},
        onAddIncomeClick = {},
        onBackClick = {}
    )
}
