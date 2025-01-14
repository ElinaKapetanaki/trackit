package com.trackit.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trackit.R
import com.trackit.ui.components.BlackButton
import com.trackit.ui.components.LinkedText
import com.trackit.ui.components.StyledTextField
import com.trackit.viewmodel.AppViewModelProvider
import com.trackit.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val loginState by loginViewModel.loginState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Background color
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        // Add the logo image with a black border
        Image(
            painter = painterResource(id = R.drawable.logo), // Use your image resource here
            contentDescription = "Logo",
            modifier = Modifier
                .padding(bottom = 24.dp, top= 50.dp) // Adjust padding as needed
            ,
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Welcome!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        StyledTextField(
            value = loginState.email,
            onValueChange = { loginViewModel.updateEmail(it) },
            label = "Email or Username"
        )

        StyledTextField(
            value = loginState.password,
            onValueChange = { loginViewModel.updatePassword(it) },
            label = "Password",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        BlackButton(
            text = "Log In",
            onClick = {
                if (loginState.email.isEmpty() || loginState.password.isEmpty()) {
                    Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                } else {
                    loginViewModel.login { isSuccess ->
                        if (isSuccess) {
                            onLoginSuccess()
                        } else {
                            Toast.makeText(context, "Invalid credentials!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        LinkedText(
            staticText = "Don't have an account?",
            clickableText = "Sign Up",
            onClick = onSignUpClick
        )
    }
}

