package com.example.activity_signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel = viewModel(),
    onHomeClick: () -> Unit,
    onChartsClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    onExchangeClick: () -> Unit,
    onEditProfileClick: () -> Unit
) {
    val profileData = viewModel.profileData
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                tonalElevation = 4.dp
            ) {
                IconButton(onClick = onHomeClick, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = onChartsClick, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = "Charts",
                        tint = Color.Black
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.Black)
                        .align(Alignment.CenterVertically)
                        .clickable { onAddButtonClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
                IconButton(onClick = onExchangeClick, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.CurrencyExchange,
                        contentDescription = "Exchange",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = onEditProfileClick, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = Color.Black
                    )
                }
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF5F5F5))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(bottom = 20.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = 25.dp)
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
                            text = profileData.fullName,
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(Color.White)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    ProfileField(
                        label = "Full Name",
                        value = profileData.fullName,
                        onValueChange = viewModel::updateFullName
                    )
                    ProfileField(
                        label = "Username",
                        value = profileData.username,
                        onValueChange = viewModel::updateUsername
                    )
                    ProfileField(
                        label = "New Password",
                        value = profileData.password,
                        onValueChange = viewModel::updatePassword,
                        isPassword = true
                    )
                    ProfileField(
                        label = "Fixed Income (€)",
                        value = profileData.fixedIncome,
                        onValueChange = viewModel::updateFixedIncome,
                        isNumeric = true
                    )
                    ProfileField(
                        label = "Fixed Expenses (€)",
                        value = profileData.fixedExpenses,
                        onValueChange = viewModel::updateFixedExpenses,
                        isNumeric = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            viewModel.saveChanges {
                                showDialog = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(3.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text("SAVE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmation") },
            text = { Text("Changes have been saved!") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK", color = Color.Black)
                }
            },
            containerColor = Color.White
        )
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

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen(
        onHomeClick = {},
        onChartsClick = {},
        onAddButtonClick = {},
        onExchangeClick = {},
        onEditProfileClick = {}
    )
}
