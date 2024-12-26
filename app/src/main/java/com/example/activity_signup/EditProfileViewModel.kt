package com.example.activity_signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// Data class for Profile
data class ProfileData(
    val fullName: String = "John Doe",
    val username: String = "john_doe",
    val password: String = "password123",
    val fixedIncome: String = "1000",
    val fixedExpenses: String = "500"
)

// ViewModel for Edit Profile Screen
class EditProfileViewModel : ViewModel() {

    // State for profile data
    var profileData = ProfileData()
        private set

    // Update methods
    fun updateFullName(newName: String) {
        profileData = profileData.copy(fullName = newName)
    }

    fun updateUsername(newUsername: String) {
        profileData = profileData.copy(username = newUsername)
    }

    fun updatePassword(newPassword: String) {
        profileData = profileData.copy(password = newPassword)
    }

    fun updateFixedIncome(newIncome: String) {
        profileData = profileData.copy(fixedIncome = newIncome)
    }

    fun updateFixedExpenses(newExpenses: String) {
        profileData = profileData.copy(fixedExpenses = newExpenses)
    }

    // Simulate saving changes
    fun saveChanges(onSaveSuccess: () -> Unit) {
        viewModelScope.launch {
            // Simulate saving to a database or API
            onSaveSuccess()
        }
    }
}
