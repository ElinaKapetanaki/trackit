package com.trackit.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trackit.ui.components.BlackButton
import com.trackit.ui.components.LinkedText
import com.trackit.ui.components.StyledTextField
import com.trackit.ui.theme.SignUpActivityTheme

@Composable
fun SignupScreen(
    onSignupComplete: () -> Unit,
    onLogInClick: () -> Unit
) {
    val context = LocalContext.current

    // Local state to hold form inputs
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Light background
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            text = "Create Your Account",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Full Name Field
        StyledTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = "Full Name"
        )

        // Email Field
        StyledTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            keyboardType = KeyboardType.Email
        )

        // Username Field
        StyledTextField(
            value = username,
            onValueChange = { username = it },
            label = "Username"
        )

        // Password Field
        StyledTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            isPassword = true
        )

        // Confirm Password Field
        StyledTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirm Password",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sign-Up Button
        BlackButton(text = "Sign Up", onClick = {
            when {
                fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                    Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                }
                password != confirmPassword -> {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(context, "Signup successful!", Toast.LENGTH_SHORT).show()
                    onSignupComplete()
                }
            }
        })

        // Login Redirect Button
        LinkedText(
            staticText = "Already have an account?",
            clickableText = "Login",
            onClick = onLogInClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    SignUpActivityTheme {
        SignupScreen(
            onSignupComplete = {},
            onLogInClick = {}
        )
    }
}
