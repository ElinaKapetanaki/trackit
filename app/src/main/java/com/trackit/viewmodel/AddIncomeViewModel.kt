package com.trackit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackit.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IncomeViewModel(
    private val repository: AppRepository,
    userSessionViewModel: UserSessionViewModel
) : ViewModel() {

    // Παρακολούθηση userId από το UserSessionViewModel
    private val userIdFlow = userSessionViewModel.userId

    // Πεδία φόρμας
    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _date = MutableStateFlow("Select Date")
    val date: StateFlow<String> = _date

    // Ενημερώσεις πεδίων φόρμας
    fun updateAmount(newAmount: String) {
        _amount.value = newAmount
    }

    fun updateDescription(newDescription: String) {
        _description.value = newDescription
    }

    fun updateDate(newDate: String) {
        _date.value = newDate
    }

    // Αποθήκευση εισοδήματος στη βάση δεδομένων
    fun saveIncome(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Έλεγχος εγκυρότητας πεδίων
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

                // Λήψη userId από το UserSessionViewModel
                val userId = userIdFlow.value
                if (userId == null) {
                    onError("User ID is null. Please log in again.")
                    return@launch
                }

                // Εισαγωγή εισοδήματος στο repository
                repository.insertIncome(
                    userId = userId,
                    amount = _amount.value.toDouble(),
                    description = _description.value,
                    date = _date.value
                )
                onSuccess()
            } catch (e: Exception) {
                onError("Failed to save income: ${e.message}")
            }
        }
    }

    // Μέθοδος εκκαθάρισης πεδίων φόρμας
    fun clearFields() {
        _amount.value = ""
        _description.value = ""
        _date.value = "Select Date"
    }
}
