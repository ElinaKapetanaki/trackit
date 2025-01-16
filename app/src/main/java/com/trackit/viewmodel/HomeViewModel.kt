package com.trackit.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackit.R
import com.trackit.repository.AppRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Transaction(
    val id: Int,
    val title: String,
    val amount: Double,
    val date: String,
    val color: Color,
    val category: String,
    val isExpense: Boolean // To differentiate between income and expense
)

class HomeViewModel(
    private val repository: AppRepository,
    private val userSessionViewModel: UserSessionViewModel
) : ViewModel() {

    // Observing user ID from session
    val userId = userSessionViewModel.userId

    // State variables
    var userName by mutableStateOf("John Doe")
        private set

    var transactions by mutableStateOf(emptyList<Transaction>())
        private set

    var incomeList by mutableStateOf(emptyList<Transaction>())
        private set

    var profileImageDrawable by mutableStateOf(R.drawable.other) // Default image for "other"
        private set

    val income: Double
        get() = incomeList.sumOf { it.amount }

    val balance: Double
        get() = income - expenses

    val expenses: Double
        get() = transactions.filter { it.isExpense }.sumOf { it.amount }

    init {
        fetchUserData()
        fetchTransactions()
        fetchIncome()
        fetchUserProfileImage()
    }

    private fun fetchUserData() {
        viewModelScope.launch {
            try {
                val currentUserId = userId.value
                if (currentUserId != null) {
                    val user = repository.findUserById(currentUserId)
                    userName = user?.fullName ?: "John Doe" // Fallback name
                }
            } catch (e: Exception) {
                userName = "Unknown User"
            }
        }
    }

    // Helper function to parse dates
    @SuppressLint("NewApi")
    private fun parseDate(date: String): LocalDate {
        val supportedFormats = listOf(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
        )

        for (formatter in supportedFormats) {
            try {
                return LocalDate.parse(date, formatter)
            } catch (_: Exception) { }
        }

        throw IllegalArgumentException("Invalid date format: $date. Supported formats are yyyy-MM-dd or dd/MM/yyyy.")
    }

    @SuppressLint("NewApi")
    private fun formatToIsoDate(inputDate: String): String {
        val supportedFormats = listOf(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
        )

        for (formatter in supportedFormats) {
            try {
                val parsedDate = LocalDate.parse(inputDate, formatter)
                return parsedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) // Format to yyyy-MM-dd
            } catch (e: Exception) {
                // Continue trying other formats
            }
        }

        throw IllegalArgumentException("Invalid date format: $inputDate. Supported formats are yyyy-MM-dd or dd/MM/yyyy.")
    }

    @SuppressLint("NewApi")
    fun mergeAndSortTransactions(): List<Transaction> {
        // Combine transactions and incomeList
        val allTransactions = transactions + incomeList

        // Parse, reformat, and sort by date (most recent first)
        return allTransactions.map { transaction ->
            transaction.copy(
                date = formatToIsoDate(transaction.date) // Ensure the date is formatted as yyyy-MM-dd
            )
        }.sortedByDescending { parseDate(it.date) } // Sort by date
    }

    private fun fetchTransactions() {
        viewModelScope.launch {
            try {
                val currentUserId = userId.value
                if (currentUserId != null) {
                    val expenses = repository.getExpensesForUser(currentUserId)
                    transactions = expenses.map { expense ->
                        Transaction(
                            id = expense.id,
                            title = expense.description,
                            amount = expense.amount,
                            date = expense.date,
                            color = getColorByCategory(expense.category),
                            category = expense.category,
                            isExpense = true // Mark all fetched data as expenses
                        )
                    }.sortedByDescending { parseDate(it.date) } // Ensure parseDate returns a LocalDate
                }
            } catch (e: Exception) {
                transactions = emptyList()
            }
        }
    }

    private fun fetchIncome() {
        viewModelScope.launch {
            try {
                val currentUserId = userId.value
                if (currentUserId != null) {
                    val incomes = repository.getIncomeForUser(currentUserId)
                    incomeList = incomes.map { income ->
                        Transaction(
                            id = income.id,
                            title = income.description,
                            amount = income.amount,
                            date = income.date,
                            color = getColorByCategory("Income"),
                            category = "Income",
                            isExpense = false
                        )
                    }.sortedByDescending { parseDate(it.date) }
                }
            } catch (e: Exception) {
                incomeList = emptyList()
            }
        }
    }

    private fun fetchUserProfileImage() {
        viewModelScope.launch {
            try {
                val currentUserId = userId.value
                if (currentUserId != null) {
                    val user = repository.findUserById(currentUserId)
                    profileImageDrawable = when (user?.gender?.lowercase()) {
                        "man" -> R.drawable.man
                        "woman" -> R.drawable.woman
                        else -> R.drawable.other
                    }
                }
            } catch (e: Exception) {
                profileImageDrawable = R.drawable.other
            }
        }
    }

    private fun getColorByCategory(category: String): Color {
        return when (category) {
            "Food" -> Color(0xFFFFCACA)
            "Supermarket" -> Color(0xFFFFF1B0)
            "Shopping" -> Color(0xFFB0DFFF)
            "Fun" -> Color(0xFFFFC2E7)
            "Others" -> Color(0xFFD3D3D3)
            "Income" -> Color(0xFFB0FFB0)
            else -> Color(0xFFCFCFCF)
        }
    }
}
