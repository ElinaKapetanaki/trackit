package com.example.activity_signup

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.example.activity_signup.database.AppDatabase
import com.example.activity_signup.database.User
import com.example.activity_signup.ui.theme.SignUpActivityTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mindrot.jbcrypt.BCrypt // Import for bcrypt

class LoginActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(this)

        CoroutineScope(Dispatchers.IO).launch {
            val hashedPassword = BCrypt.hashpw("password123", BCrypt.gensalt())
            val user = User(emailOrUsername = "testUser", passwordHash = hashedPassword)
            database.userDao().insertUser(user)
        }

        setContent {
            SignUpActivityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(modifier = Modifier.padding(innerPadding)) { emailOrUsername, password ->
                        CoroutineScope(Dispatchers.IO).launch {
                            val user = database.userDao().findUserByUsername(emailOrUsername)
                            withContext(Dispatchers.Main) {
                                if (user != null && BCrypt.checkpw(password, user.passwordHash)) { // Secure password check
                                    Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@LoginActivity, "Invalid credentials!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}