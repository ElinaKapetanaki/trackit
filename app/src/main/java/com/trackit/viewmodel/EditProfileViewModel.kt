package com.trackit.viewmodel


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class EditProfileViewModel : ViewModel() {
    val profileData = ProfileData(
        fullName = mutableStateOf("John Doe"),
        username = mutableStateOf("johndoe123"),
        password = mutableStateOf(""),
        fixedIncome = mutableStateOf("1000"),
        fixedExpenses = mutableStateOf("500")
    )

    fun updateFullName(newName: String) {
        profileData.fullName.value = newName
    }

    fun updateUsername(newUsername: String) {
        profileData.username.value = newUsername
    }

    fun updatePassword(newPassword: String) {
        profileData.password.value = newPassword
    }

    fun updateFixedIncome(newIncome: String) {
        profileData.fixedIncome.value = newIncome
    }

    fun updateFixedExpenses(newExpenses: String) {
        profileData.fixedExpenses.value = newExpenses
    }

    fun saveChanges(onSaveComplete: () -> Unit) {
        // Handle saving logic here
        onSaveComplete()
    }
}

data class ProfileData(
    val fullName: MutableState<String>,
    val username: MutableState<String>,
    val password: MutableState<String>,
    val fixedIncome: MutableState<String>,
    val fixedExpenses: MutableState<String>
)
