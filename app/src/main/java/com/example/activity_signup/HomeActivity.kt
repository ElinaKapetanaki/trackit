package com.example.activity_signup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.activity_signup.ui.theme.SignUpActivityTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignUpActivityTheme {
                HomeScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { /* Could open a drawer or settings */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Navigate to Add Page */ },
                containerColor = Color.Black,
                contentColor = Color.White,
                modifier = Modifier.size(70.dp)
            ) {
                Text("+", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF5F5F5))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // A black card housing the welcome header and the balance card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp) // set a smaller height
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black)
                        .padding(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        WelcomeHeader("John Doe", textColor = Color.White, iconTint = Color.White)
                        Spacer(modifier = Modifier.height(16.dp))
                        BalanceCard(balance = 4800.00, income = 2500.00, expenses = 800.00)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // A black card for Transactions
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp) // set a smaller height
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black)
                        .padding(16.dp)
                ) {
                    TransactionList()
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
        IconButton(onClick = { /* Settings logic */ }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = iconTint
            )
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
fun TransactionList() {
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

        // Mock Data
        val transactions = listOf(
            Transaction("Ice Cream", -10.00, "Today", Color(0xFFFFCCCB), "Food"),
            Transaction("Super Market", -80.00, "Today", Color(0xFFAECBFA), "Shopping"),
            Transaction("Cinema", -20.00, "Yesterday", Color(0xFFF8BBE8), "Entertainment"),
            Transaction("Freelance Work", 1200.00, "This Month", Color(0xFFB8E4C9), "Work")
        )

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

data class Transaction(
    val title: String,
    val amount: Double,
    val date: String,
    val color: Color,
    val category: String
)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SignUpActivityTheme {
        HomeScreen()
    }
}
