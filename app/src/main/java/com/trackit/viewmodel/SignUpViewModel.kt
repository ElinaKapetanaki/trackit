package com.trackit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackit.database.User
import com.trackit.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class SignUpViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> = _signUpState

    // Update full name field
    fun updateFullName(fullName: String) {
        _signUpState.update { it.copy(fullName = fullName) }
    }

    // Update email field
    fun updateEmail(email: String) {
        _signUpState.update { it.copy(email = email) }
    }

    // Update username field
    fun updateUsername(username: String) {
        _signUpState.update { it.copy(username = username) }
    }

    // Update password field
    fun updatePassword(password: String) {
        _signUpState.update { it.copy(password = password) }
    }

    // Update confirm password field
    fun updateConfirmPassword(confirmPassword: String) {
        _signUpState.update { it.copy(confirmPassword = confirmPassword) }
    }

    // Perform sign-up
    fun signUp(onResult: (Boolean) -> Unit) {
        val state = _signUpState.value
        if (state.fullName.isBlank() || state.email.isBlank() || state.username.isBlank() || state.password.isBlank() || state.confirmPassword.isBlank()) {
            _signUpState.update { it.copy(errorMessage = "All fields must be filled out") }
            onResult(false)
            return
        }

        if (state.password != state.confirmPassword) {
            _signUpState.update { it.copy(errorMessage = "Passwords do not match") }
            onResult(false)
            return
        }

        val hashedPassword = BCrypt.hashpw(state.password, BCrypt.gensalt())
        val newUser = User(
            fullName = state.fullName,
            emailOrUsername = state.username,
            passwordHash = hashedPassword
        )

        viewModelScope.launch {
            val userExists = repository.findUserByUsername(state.username) != null
            if (userExists) {
                _signUpState.update { it.copy(errorMessage = "Username already exists") }
                onResult(false)
            } else {
                repository.insertUser(newUser)
                _signUpState.update { it.copy(isSuccess = true, errorMessage = null) }
                onResult(true)
            }
        }
    }
}

// Data class for managing sign-up state
data class SignUpState(
    val fullName: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
