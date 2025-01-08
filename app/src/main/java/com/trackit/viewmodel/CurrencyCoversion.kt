package com.trackit.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackit.repository.CurrencyApiHandler
import com.trackit.repository.ExchangeRatesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencyViewModel : ViewModel() {

    private val apiHandler = CurrencyApiHandler()
    var conversionResult: String = ""
    var isLoading: Boolean = false

    fun fetchExchangeRates(baseCurrency: String, targetCurrency: String, apiKey: String, amount: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                val result = apiHandler.getExchangeRates(baseCurrency, apiKey)
                result.onSuccess { response ->
                    val rate = response.rates?.get(targetCurrency)
                    if (rate != null) {
                        val convertedAmount = amount * rate
                        conversionResult = "Result: %.2f $targetCurrency".format(convertedAmount)
                    } else {
                        conversionResult = "Conversion rate not available."
                    }
                }.onFailure { error ->
                    Log.e("CurrencyViewModel", "Error: ${error.message}")
                    conversionResult = "Error: ${error.message}"
                }
            } catch (e: Exception) {
                Log.e("CurrencyViewModel", "Unexpected Error: ${e.message}")
                conversionResult = "Unexpected Error Occurred."
            } finally {
                isLoading = false
            }
        }
    }
}
