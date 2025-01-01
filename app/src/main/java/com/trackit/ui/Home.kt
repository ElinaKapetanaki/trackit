package com.trackit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trackit.ui.components.BottomNavBar
import com.trackit.ui.components.PageTopBar
import com.trackit.ui.theme.SignUpActivityTheme
import com.trackit.viewmodel.HomeViewModel
import com.trackit.viewmodel.Transaction


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    successMessage: String?,
    onHomeClick: () -> Unit,
    onChartsClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    onExchangeClick: () -> Unit,
    onEditProfileClick: () -> Unit) {
    Scaffold(
        topBar = {
            PageTopBar(
                title = "Dashboard"
            )
        },
        bottomBar = {
            BottomNavBar(
                onHomeClick = onHomeClick,
                onChartsClick = onChartsClick,
                onAddButtonClick = onAddButtonClick,
                onExchangeClick = onExchangeClick,
                onEditProfileClick = onEditProfileClick
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF5F5F5))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                successMessage?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFB8E4C9)) // Pale Green Color
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(it, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black)
                        .padding(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        WelcomeHeader(
                            userName = viewModel.userName,
                            textColor = Color.White,
                            iconTint = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        BalanceCard(
                            balance = viewModel.balance,
                            income = viewModel.income,
                            expenses = viewModel.expenses
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black)
                        .padding(16.dp)
                ) {
                    TransactionList(transactions = viewModel.transactions)
                }
            }
        }
    )
}


@Composable
fun WelcomeHeader(userName: String, textColor: Color = Color.White, iconTint: Color = Color.White) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Welcome,", fontSize = 18.sp, color = textColor)
            Text(userName, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = textColor)
        }
    }
}

@Composable
fun BalanceCard(balance: Double, income: Double, expenses: Double) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Total Balance", fontSize = 18.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$${"%.2f".format(balance)}",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Income: $${"%.2f".format(income)}", color = Color(0xFF98FB98), fontSize = 14.sp)
                Text("Expenses: $${"%.2f".format(expenses)}", color = Color(0xFFFFC0CB), fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun TransactionList(transactions: List<Transaction>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Transactions", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            TextButton(onClick = { /* View All Transactions Logic */ }) {
                Text("View All", color = Color.LightGray)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        transactions.forEach {
            Spacer(modifier = Modifier.height(8.dp))
            TransactionItem(it)
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(transaction.color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = transaction.category.first().toString(),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(transaction.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(transaction.date, fontSize = 14.sp, color = Color.Gray)
        }
        Text(
            text = if (transaction.amount > 0) "+€${"%.2f".format(transaction.amount)}" else "€${"%.2f".format(transaction.amount)}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (transaction.amount > 0) Color(0xFF4CAF50) else Color(0xFFFF5722)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SignUpActivityTheme {
        HomeScreen(
            successMessage = "Expense successfully added!", onAddButtonClick = {},
            onHomeClick = {},
            onChartsClick = {},
            onExchangeClick = {},
            onEditProfileClick = {},
        )
    }
}
