package com.trackit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trackit.ui.components.BottomNavBar
import com.trackit.viewmodel.CurrencyViewModel
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyConversionScreen(
    viewModel: CurrencyViewModel = viewModel(),
    onHomeClick: () -> Unit,
    onChartsClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    onExchangeClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onResultClick: (String) -> Unit // Navigate to the result screen
) {
    var amount by remember { mutableStateOf("") }
    var fromCurrency by remember { mutableStateOf("EUR") }
    var toCurrency by remember { mutableStateOf("USD") }
    val apiKey = "96a2d297cad9caf2acab72c637562516"

    // Observe the conversion result and loading state
    val conversionResult by remember { viewModel.conversionResult }
    val isLoading by remember { viewModel.isLoading }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = onHomeClick) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Currency Converter", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Black
                )
            )
        },
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF5F5F5))
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                            onValueChange = { input ->
                                if (input.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                    amount = input
                                }
                            },
                            placeholder = {
                                Text("50", color = Color.LightGray, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            },
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.LightGray,
                                unfocusedIndicatorColor = Color.Gray
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // From Currency Dropdown
                    Text("From Currency", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    DropdownMenuWithOptions(
                        options = listOf("EUR"),
                        selectedOption = fromCurrency,
                        onSelected = { fromCurrency = it },
                        backgroundColor = Color.DarkGray,
                        textColor = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // To Currency Dropdown
                    Text("To Currency", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    DropdownMenuWithOptions(
                        options = listOf("USD", "GBP", "INR"),
                        selectedOption = toCurrency,
                        onSelected = { toCurrency = it },
                        backgroundColor = Color.DarkGray,
                        textColor = Color.White
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Convert Button
                    Button(
                        onClick = {
                            val amountDouble = amount.toDoubleOrNull()
                            if (amountDouble != null) {
                                viewModel.fetchExchangeRates(fromCurrency, toCurrency, apiKey, amountDouble)
                                onResultClick(conversionResult) // Navigate to result screen
                            } else {
                                viewModel.conversionResult.value = "Invalid amount entered."
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(if (isLoading) "Converting..." else "Convert")
                    }
                }
            }
        }
    )
}
