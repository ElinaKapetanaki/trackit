package com.trackit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.trackit.ui.components.BlackButton
import com.trackit.ui.components.BottomNavBar
import com.trackit.ui.components.StyledTextField
import com.trackit.viewmodel.AppViewModelProvider
import com.trackit.viewmodel.EditProfileViewModel

@Composable
fun EditProfileScreen(
    onHomeClick: () -> Unit,
    onChartsClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    onExchangeClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    viewModel: EditProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val profileState by viewModel.profileState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                onHomeClick = onHomeClick,
                onChartsClick = onChartsClick,
                onAddButtonClick = onAddButtonClick,
                onExchangeClick = onExchangeClick,
                onEditProfileClick = onEditProfileClick
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF5F5F5))
            ) {
                // Header
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

                        // Profile Image
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = profileState.genderDrawable,
                                contentDescription = "Profile Picture",
                                modifier = Modifier.clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = profileState.fullName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = profileState.username,
                            fontSize = 14.sp,
                            color = Color.LightGray
                        )
                    }
                }

                // Form
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(Color.White)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    StyledTextField(
                        label = "Full Name",
                        value = profileState.fullName,
                        onValueChange = viewModel::updateFullName
                    )
                    StyledTextField(
                        label = "Username",
                        value = profileState.username,
                        onValueChange = viewModel::updateUsername
                    )
                    StyledTextField(
                        label = "New Password",
                        value = profileState.password,
                        onValueChange = viewModel::updatePassword,
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    BlackButton(text = "Save", onClick = {
                        viewModel.saveChanges(
                            onSaveComplete = { showDialog = true },
                            onError = { message -> errorMessage = message }
                        )
                    })
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

    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            title = { Text("Error") },
            text = { Text(errorMessage!!) },
            confirmButton = {
                TextButton(onClick = { errorMessage = null }) {
                    Text("OK", color = Color.Black)
                }
            },
            containerColor = Color.White
        )
    }
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
