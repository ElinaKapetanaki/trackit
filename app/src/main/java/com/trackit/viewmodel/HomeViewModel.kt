package com.trackit.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackit.repository.AppRepository
import kotlinx.coroutines.launch

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

    var profileImageUri by mutableStateOf<String?>(null)
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

    /*
     * Fetch user-specific data such as name or profile information.
     */
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

    /*
     * Fetch transactions for the logged-in user from the database.
     */
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
                    }
                }
            } catch (e: Exception) {
                transactions = emptyList()
            }
        }
    }

    /*
     * Fetch income for the logged-in user from the database.
     */
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
                            isExpense = false // Mark all fetched data as income
                        )
                    }
                }
            } catch (e: Exception) {
                incomeList = emptyList()
            }
        }
    }

    /*
     * Fetch the user's profile image URI.
     */
    private fun fetchUserProfileImage() {
        viewModelScope.launch {
            try {
                val currentUserId = userId.value
                if (currentUserId != null) {
                    val user = repository.findUserById(currentUserId)
                    profileImageUri = user?.profileImageUri // Get the image URI
                }
            } catch (e: Exception) {
                profileImageUri = null
            }
        }
    }

    /*
     * Helper function to determine color based on the category.
     */
    private fun getColorByCategory(category: String): Color {
        return when (category) {
            "Food" -> Color(0xFFFFE5E5)            // Pale Red
            "Supermarket" -> Color(0xFFFFF9E6)     // Pale Yellow
            "Shopping" -> Color(0xFFE6F0FF)        // Pale Blue
            "Fun" -> Color(0xFFFDE6F2)             // Pale Pink
            "Others" -> Color(0xFFE6E6E6)          // Pale Gray
            "Income" -> Color(0xFFE6FFE6)          // Pale Green
            else -> Color(0xFFD9D9D9)              // Default Pale Gray
        }
    }
}
