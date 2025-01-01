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

class LoginViewModel(private val repository: AppRepository) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState


    fun updateEmail(email: String) {
        _loginState.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _loginState.update { it.copy(password = password) }
    }

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
            onResult(isSuccess)
        }
    }

    fun insertTestUser() {
        val hashedPassword = BCrypt.hashpw("password123", BCrypt.gensalt())
        val testUser = User(emailOrUsername = "testUser", passwordHash = hashedPassword)

        viewModelScope.launch {
            repository.insertUser(testUser)
        }
    }
}

data class LoginState(
    val email: String = "",
    val password: String = ""
)
