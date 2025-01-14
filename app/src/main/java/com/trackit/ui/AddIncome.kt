package com.trackit.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trackit.ui.components.BlackButton
import com.trackit.ui.components.PageTopBar
import com.trackit.viewmodel.AppViewModelProvider
import com.trackit.viewmodel.IncomeViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIncomeScreen(
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    incomeViewModel: IncomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Παρακολούθηση καταστάσεων από το ViewModel
    val amount by incomeViewModel.amount.collectAsState()
    val description by incomeViewModel.description.collectAsState()
    val date by incomeViewModel.date.collectAsState()

    // DatePickerDialog setup
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                incomeViewModel.updateDate(formattedDate)
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            PageTopBar(
                title = "Add Income",
                onBackClick = onBackClick
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF5F5F5))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Black)
                            .padding(16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Amount Input
                        Text("Amount", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("€", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            TextField(
                                value = amount,
                                onValueChange = {
                                    // Επιτρέπουμε μόνο αριθμούς και δεκαδικά
                                    if (it.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                        incomeViewModel.updateAmount(it)
                                    }
                                },
                                placeholder = { Text("100", color = Color.LightGray) },
                                textStyle = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.LightGray,
                                    unfocusedIndicatorColor = Color.Gray
                                ),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                ),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                        // Description Input
                        Text("Description", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = description,
                            onValueChange = { incomeViewModel.updateDescription(it) },
                            placeholder = { Text("Salary payment", color = Color.LightGray) },
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.White),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.LightGray,
                                unfocusedIndicatorColor = Color.Gray
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        // Date Picker
                        Text("Date", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { datePickerDialog.show() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                        ) {
                            Text(date, color = Color.White, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Save Button
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        BlackButton(
                            text = "Save",
                            onClick = {
                                coroutineScope.launch {
                                    when {
                                        amount.isBlank() -> {
                                            snackbarHostState.showSnackbar("Amount cannot be empty.")
                                        }
                                        amount.toDoubleOrNull() == null -> {
                                            snackbarHostState.showSnackbar("Please enter a valid numeric amount.")
                                        }
                                        description.isBlank() -> {
                                            snackbarHostState.showSnackbar("Description cannot be empty.")
                                        }
                                        date == "Select Date" -> {
                                            snackbarHostState.showSnackbar("Please select a valid date.")
                                        }
                                        else -> {
                                            // Κλήση της saveIncome μέσα σε coroutineScope
                                            incomeViewModel.saveIncome(
                                                onSuccess = {
                                                        onSaveClick()
                                                },
                                                onError = { message ->
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(message)
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            },
                            cornerRadius = 20
                        )
                    }
                }
            }
        }
    )
}
