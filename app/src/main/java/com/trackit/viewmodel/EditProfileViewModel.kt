package com.trackit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackit.R
import com.trackit.database.User
import com.trackit.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val repository: AppRepository,
    private val userSessionViewModel: UserSessionViewModel
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState

    init {
        fetchUserProfile()
    }

    // Fetch user profile details
    private fun fetchUserProfile() {
        viewModelScope.launch {
            val userId = userSessionViewModel.userId.value
            if (userId != null) {
                val user = repository.findUserById(userId)
                user?.let {
                    _profileState.value = ProfileState(
                        fullName = it.fullName,
                        username = it.emailOrUsername,
                        password = "",
                        gender = it.gender
                    )
                }
            }
        }
    }

    // Update full name
    fun updateFullName(newFullName: String) {
        _profileState.update { it.copy(fullName = newFullName) }
    }

    // Update username
    fun updateUsername(newUsername: String) {
        _profileState.update { it.copy(username = newUsername) }
    }

    // Update password
    fun updatePassword(newPassword: String) {
        _profileState.update { it.copy(password = newPassword) }
    }

    // Save changes to the profile
    fun saveChanges(onSaveComplete: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val userId = userSessionViewModel.userId.value
            if (userId == null) {
                onError("User ID not found. Please log in again.")
                return@launch
            }

            val state = _profileState.value
            if (state.fullName.isBlank() || state.username.isBlank()) {
                onError("Full Name and Username cannot be empty.")
                return@launch
            }

            val updatedUser = User(
                id = userId,
                fullName = state.fullName,
                emailOrUsername = state.username,
                passwordHash = if (state.password.isNotBlank()) {
                    org.mindrot.jbcrypt.BCrypt.hashpw(state.password, org.mindrot.jbcrypt.BCrypt.gensalt())
                } else {
                    repository.findUserById(userId)?.passwordHash ?: ""
                },
                gender = state.gender // Keep the current gender
            )

            repository.insertUser(updatedUser)
            onSaveComplete()
        }
    }
}

// Data class for managing profile state
data class ProfileState(
    val fullName: String = "",
    val username: String = "",
    val password: String = "",
    val gender: String = "other" // Default gender
) {
    // Compute the drawable resource based on gender
    val genderDrawable: Int
        get() = when (gender.lowercase()) {
            "man" -> R.drawable.man
            "woman" -> R.drawable.woman
            else -> R.drawable.other
        }
}
