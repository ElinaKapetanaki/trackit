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

class LoginViewModel(
    private val repository: AppRepository,
    private val userSessionViewModel: UserSessionViewModel
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    // Update email field
    fun updateEmail(email: String) {
        _loginState.update { it.copy(email = email) }
    }

    // Update password field
    fun updatePassword(password: String) {
        _loginState.update { it.copy(password = password) }
    }

    // Perform login and update the user session
    fun login(onResult: (Boolean) -> Unit) {
        val email = _loginState.value.email
        val password = _loginState.value.password

        if (email.isBlank() || password.isBlank()) {
            onResult(false)
            return
        }

        viewModelScope.launch {
            val user = repository.findUserByUsername(email)
            val isSuccess = user != null && BCrypt.checkpw(password, user.passwordHash)
            if (isSuccess && user != null) {
                userSessionViewModel.setUserId(user.id) // Save user ID in session
            }
            onResult(isSuccess)
        }
    }

    // Insert test users for development purposes
    fun insertTestUser() {
        /* delete test user
        viewModelScope.launch {
            repository.deleteUserExpensesByUsername("testUser")
            repository.deleteUserByUsername("testUser")
        }*/

        val hashedPassword = BCrypt.hashpw("password123", BCrypt.gensalt())
        val testUser = User(fullName = "Test User", emailOrUsername = "testUser", passwordHash = hashedPassword)

        viewModelScope.launch {
            repository.insertUser(testUser)
        }

        val hashedPassword2 = BCrypt.hashpw("123", BCrypt.gensalt())
        val testUser2 = User(fullName = "Elina Kapetanaki", emailOrUsername = "elina", passwordHash = hashedPassword2)
        viewModelScope.launch {
            repository.insertUser(testUser2)
        }
    }
}

// Data class for managing login state
data class LoginState(
    val email: String = "",
    val password: String = ""
)
