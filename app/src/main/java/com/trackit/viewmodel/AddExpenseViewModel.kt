package com.trackit.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Calendar

class AddExpenseViewModel : ViewModel() {

    // State for inputs
    var amount by mutableStateOf("")
        private set
    var category by mutableStateOf("Supermarket")
        private set
    var description by mutableStateOf("")
        private set
    var selectedDate by mutableStateOf("Select Date")
        private set
    var selectedCategory by mutableStateOf("Food")
        private set


    val calendar: Calendar = Calendar.getInstance()

    // List of categories
    val categories = listOf("Food", "Supermarket", "Shopping", "Fun", "Others")

    // Update functions
    fun onAmountChange(newAmount: String) {
        amount = newAmount
    }

    fun onDescriptionChange(newDescription: String) {
        description = newDescription
    }

    fun saveExpense() {
        println("Expense Saved: $amount, $category, $description, $selectedDate")
    }

    fun onDateSelected(date: String) {
        selectedDate = date
    }

    fun onCategorySelected(category: String) {
        selectedCategory = category
    }

}
