package com.example.activity_signup

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

data class Transaction(
    val title: String,
    val amount: Double,
    val date: String,
    val color: Color,
    val category: String
)

class HomeViewModel : ViewModel() {
    var userName by mutableStateOf("John Doe")
        private set

    var transactions by mutableStateOf(
        listOf(
            Transaction("Ice Cream", -10.00, "Today", Color(0xFFFFCCCB), "Food"),
            Transaction("Super Market", -80.00, "Today", Color(0xFFAECBFA), "Shopping"),
            Transaction("Cinema", -20.00, "Yesterday", Color(0xFFF8BBE8), "Entertainment"),
            Transaction("Freelance Work", 1200.00, "This Month", Color(0xFFB8E4C9), "Work")
        )
    )
        private set

    val balance: Double
        get() = transactions.sumOf { it.amount }

    val income: Double
        get() = transactions.filter { it.amount > 0 }.sumOf { it.amount }

    val expenses: Double
        get() = transactions.filter { it.amount < 0 }.sumOf { -it.amount }

    var isLoading by mutableStateOf(true)
        private set

    fun addTransaction(transaction: Transaction) {
        transactions = transactions + transaction
    }

}
