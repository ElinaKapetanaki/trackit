package com.trackit.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trackit.VideoActivity
import com.trackit.ui.components.BlackButton
import com.trackit.ui.components.LinkedText
import com.trackit.ui.components.StyledTextField
import com.trackit.ui.theme.SignUpActivityTheme
import com.trackit.viewmodel.AppViewModelProvider
import com.trackit.viewmodel.SignUpViewModel

@Composable
fun SignupScreen(
    onSignupComplete: () -> Unit,
    onLogInClick: () -> Unit,
    signUpViewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val signUpState by signUpViewModel.signUpState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create Your Account",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        StyledTextField(
            value = signUpState.fullName,
            onValueChange = { signUpViewModel.updateFullName(it) },
            label = "Full Name"
        )

        StyledTextField(
            value = signUpState.email,
            onValueChange = { signUpViewModel.updateEmail(it) },
            label = "Email"
        )

        StyledTextField(
            value = signUpState.username,
            onValueChange = { signUpViewModel.updateUsername(it) },
            label = "Username"
        )

        StyledTextField(
            value = signUpState.password,
            onValueChange = { signUpViewModel.updatePassword(it) },
            label = "Password",
            isPassword = true
        )

        StyledTextField(
            value = signUpState.confirmPassword,
            onValueChange = { signUpViewModel.updateConfirmPassword(it) },
            label = "Confirm Password",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        BlackButton(
            text = "Sign Up",
            onClick = {
                signUpViewModel.signUp { isSuccess, errorMessage ->
                    if (isSuccess) {
                        Toast.makeText(context, "Signup Successful!", Toast.LENGTH_SHORT).show()

                        // Εκκίνηση του VideoActivity
                        val intent = Intent(context, VideoActivity::class.java)
                        context.startActivity(intent)

                        // Κλήση του callback για ολοκλήρωση
                        onSignupComplete()
                    } else {
                        Toast.makeText(context, errorMessage ?: "Sign up failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

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
        SignupScreen(onSignupComplete = {}, onLogInClick = {})
    }
}
