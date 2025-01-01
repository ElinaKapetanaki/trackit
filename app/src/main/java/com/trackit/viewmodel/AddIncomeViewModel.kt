package com.trackit.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Calendar

class AddIncomeViewModel : ViewModel() {

    // State for inputs
    var amount by mutableStateOf("")
        private set
    var selectedDate by mutableStateOf("Select Date")
        private set
    val calendar: Calendar = Calendar.getInstance()

    // Update functions
    fun onAmountChange(newAmount: String) {
        amount = newAmount
    }

    fun onDateSelected(date: String) {
        selectedDate = date
    }

    fun saveIncome() {
        println("Income Saved: $amount on $selectedDate")
    }
}
