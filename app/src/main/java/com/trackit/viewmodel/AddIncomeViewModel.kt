package com.trackit.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackit.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class IncomeViewModel(
    private val repository: AppRepository,
    userSessionViewModel: UserSessionViewModel
) : ViewModel() {

    private val userIdFlow = userSessionViewModel.userId

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _date = MutableStateFlow("Select Date")
    val date: StateFlow<String> = _date

    fun updateAmount(newAmount: String) {
        _amount.value = newAmount
    }

    fun updateDescription(newDescription: String) {
        _description.value = newDescription
    }

    fun updateDate(newDate: String) {
        _date.value = newDate
    }

    @SuppressLint("NewApi")
    fun formatToIsoDate(inputDate: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.parse(inputDate, inputFormatter).format(outputFormatter)
    }

    fun saveIncome(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
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

                // Ensure date is in the correct ISO format (yyyy-MM-dd)
                val formattedDate = try {
                    formatToIsoDate(_date.value)
                } catch (e: Exception) {
                    onError("Invalid date format. Please use dd/MM/yyyy.")
                    return@launch
                }

                if (_description.value.isBlank()) {
                    onError("Description cannot be empty.")
                    return@launch
                }

                val userId = userIdFlow.value
                if (userId == null) {
                    onError("User ID is null. Please log in again.")
                    return@launch
                }

                repository.insertIncome(
                    userId = userId,
                    amount = _amount.value.toDouble(),
                    description = _description.value,
                    date = formattedDate
                )
                onSuccess()
            } catch (e: Exception) {
                onError("Failed to save income: ${e.message}")
            }
        }
    }
}
