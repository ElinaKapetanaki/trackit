package com.trackit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackit.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExpenseViewModel(
    private val repository: AppRepository,
    userSessionViewModel: UserSessionViewModel
) : ViewModel() {

    // Observe userId from UserSessionViewModel
    private val userIdFlow = userSessionViewModel.userId

    // Form fields
    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _category = MutableStateFlow("Food")
    val category: StateFlow<String> = _category

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _date = MutableStateFlow("Select Date")
    val date: StateFlow<String> = _date

    private val _categories = MutableStateFlow(listOf("Food", "Supermarket", "Shopping", "Fun", "Others"))
    val categories: StateFlow<List<String>> = _categories

    // Functions to update form fields
    fun updateAmount(newAmount: String) {
        _amount.value = newAmount
    }

    fun updateCategory(newCategory: String) {
        _category.value = newCategory
    }

    fun updateDescription(newDescription: String) {
        _description.value = newDescription
    }

    fun updateDate(newDate: String) {
        _date.value = newDate
    }

    // Save the expense to the database
    fun saveExpense(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Validate fields
                if (_amount.value.isBlank()) {
                    onError("Amount cannot be empty.")
                    return@launch
                }

                if (_amount.value.toDoubleOrNull() == null) {
                    onError("Please enter a valid numeric amount.")
                    return@launch
                }

                if (_date.value == "Select Date") {
                    onError("Please select a valid date.")
                    return@launch
                }

                if (_description.value.isBlank()) {
                    onError("Description cannot be empty.")
                    return@launch
                }

                // Collect userId from userSessionViewModel
                val userId = userIdFlow.value
                if (userId == null) {
                    onError("User ID is null. Please log in again.")
                    return@launch
                }

                // Insert expense into the repository
                repository.insertExpense(
                    userId = userId,
                    amount = _amount.value.toDouble(),
                    category = _category.value,
                    description = _description.value,
                    date = _date.value
                )
                onSuccess()
            } catch (e: Exception) {
                onError("Failed to save expense: ${e.message}")
            }
        }
    }
}
