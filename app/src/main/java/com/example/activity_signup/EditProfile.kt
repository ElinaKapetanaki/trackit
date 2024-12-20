package com.example.activity_signup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.VisualTransformation


class EditProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EditProfileScreen()
        }
    }
}

@Composable
fun EditProfileScreen() {
    var fullName by remember { mutableStateOf("John Doe") }
    var username by remember { mutableStateOf("john_doe") }
    var password by remember { mutableStateOf("password123") }
    var fixedIncome by remember { mutableStateOf("1000") }
    var fixedExpenses by remember { mutableStateOf("500") }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Light background
    ) {
        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(bottom = 40.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Text(
                    text = "Profile",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Your\nPhoto", color = Color.White, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = fullName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "your_email@example.com",
                    fontSize = 14.sp,
                    color = Color.LightGray
                )
            }
        }

        // Main Content Section
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Full Name Field
            ProfileField(
                label = "Full Name",
                value = fullName,
                onValueChange = { fullName = it }
            )

            // Username Field
            ProfileField(
                label = "Username",
                value = username,
                onValueChange = { username = it }
            )

            // Password Field
            ProfileField(
                label = "New Password",
                value = password,
                onValueChange = { password = it },
                isPassword = true
            )

            // Fixed Income Field
            ProfileField(
                label = "Fixed Income (€)",
                value = fixedIncome,
                onValueChange = { fixedIncome = it },
                isNumeric = true
            )

            // Fixed Expenses Field
            ProfileField(
                label = "Fixed Expenses (€)",
                value = fixedExpenses,
                onValueChange = { fixedExpenses = it },
                isNumeric = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black )
            ) {
                Text("SAVE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        // Confirmation Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmation") },
                text = { Text("Changes have been saved!\nIncome: €$fixedIncome\nExpenses: €$fixedExpenses") },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("OK", color = Color.Black)
                    }
                },
                containerColor = Color.White
            )
        }
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    isNumeric: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = when {
            isPassword -> KeyboardOptions(keyboardType = KeyboardType.Password)
            isNumeric -> KeyboardOptions(keyboardType = KeyboardType.Number)
            else -> KeyboardOptions.Default
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Preview(showBackground = true, name = "Edit Profile Preview")
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen()
}
