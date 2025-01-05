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
import coil.compose.AsyncImage
import com.trackit.ui.components.BottomNavBar
import com.trackit.ui.components.PageTopBar
import com.trackit.ui.theme.SignUpActivityTheme
import com.trackit.viewmodel.AppViewModelProvider
import com.trackit.viewmodel.HomeViewModel
import com.trackit.viewmodel.Transaction
import com.trackit.R
import androidx.compose.ui.res.painterResource

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    successMessage: String?,
    onHomeClick: () -> Unit,
    onChartsClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    onExchangeClick: () -> Unit,
    onEditProfileClick: () -> Unit
) {
    // Observing data from the ViewModel
    val transactions = viewModel.transactions + viewModel.incomeList // Combine expenses and incomes
    val balance = viewModel.balance
    val income = viewModel.income
    val expenses = viewModel.expenses
    val userName = viewModel.userName
    val profileImageDrawable = viewModel.profileImageDrawable


    Scaffold(
        topBar = {
            PageTopBar(title = "Dashboard")
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
                            .background(Color(0xFFB8E4C9))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(it, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Balance and User Info
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black)
                        .padding(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Profile Picture
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = profileImageDrawable,
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier.fillMaxSize(),
                                    placeholder = painterResource(id = R.drawable.placeholder),
                                    error = painterResource(id = R.drawable.error)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp)) // Space between the image and welcome text

                            // WelcomeHeader
                            WelcomeHeader(
                                userName = userName,
                                textColor = Color.White,
                                iconTint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        BalanceCard(
                            balance = balance,
                            income = income,
                            expenses = expenses
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Transactions List
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black)
                        .padding(16.dp)
                ) {
                    // Show Transactions or Empty State
                    if (transactions.isEmpty()) {
                        EmptyState()
                    } else {
                        TransactionList(transactions = transactions)
                    }
                }
            }
        }
    )
}



@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "You don’t have any transactions yet.",
            fontSize = 16.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Use the '+' button below to add your first expense or income.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.LightGray
        )
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
            text = if (transaction.isExpense) "-€${"%.2f".format(transaction.amount)}"
            else "+€${"%.2f".format(transaction.amount)}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (transaction.isExpense) Color(0xFFFF5722) else Color(0xFF4CAF50)
        )
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
                text = "€${"%.2f".format(balance)}",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Income: €${"%.2f".format(income)}", color = Color(0xFF98FB98), fontSize = 14.sp)
                Text("Expenses: €${"%.2f".format(expenses)}", color = Color(0xFFFFC0CB), fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun WelcomeHeader(userName: String, textColor: Color = Color.White, iconTint: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
        }
        Spacer(modifier = Modifier.height(8.dp))

        transactions.forEach {
            Spacer(modifier = Modifier.height(8.dp))
            TransactionItem(it)
        }
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
