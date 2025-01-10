package com.trackit.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackit.repository.CurrencyApiHandler
import com.trackit.repository.ExchangeRatesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencyViewModel : ViewModel() {

    private val apiHandler = CurrencyApiHandler()

    // Use mutableStateOf for conversionResult and isLoading, without delegation
    var conversionResult = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    fun fetchExchangeRates(baseCurrency: String, targetCurrency: String, apiKey: String, amount: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.value = true
            try {
                val result = apiHandler.getExchangeRates(baseCurrency, apiKey)
                result.onSuccess { response ->
                    val rate = response.rates?.get(targetCurrency)
                    if (rate != null) {
                        val convertedAmount = amount * rate
                        conversionResult.value = "Result: %.2f $targetCurrency".format(convertedAmount)
                    } else {
                        conversionResult.value = "Conversion rate not available."
                    }
                }.onFailure { error ->
                    Log.e("CurrencyViewModel", "Error: ${error.message}")
                    conversionResult.value = "Error: ${error.message}"
                }
            } catch (e: Exception) {
                Log.e("CurrencyViewModel", "Unexpected Error: ${e.message}")
                conversionResult.value = "Unexpected Error Occurred."
            } finally {
                isLoading.value = false
            }
        }
    }
}
