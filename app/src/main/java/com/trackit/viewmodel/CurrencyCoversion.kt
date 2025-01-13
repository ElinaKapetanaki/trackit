package com.trackit.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackit.repository.CurrencyApiHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class CurrencyViewModel : ViewModel() {

    private val apiHandler = CurrencyApiHandler()

    var conversionResult = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    fun fetchExchangeRates(baseCurrency: String, targetCurrency: String, apiKey: String, amount: Double, onResultUpdated: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.value = true
            try {
                val result = apiHandler.getExchangeRates(baseCurrency, apiKey)
                result.onSuccess { response ->
                    val rate = response.rates?.get(targetCurrency)
                    if (rate != null) {
                        val convertedAmount = amount * rate
                        val resultString = "Result: %.2f $targetCurrency".format(convertedAmount)

                        // Ενημερώνουμε το UI στο κύριο νήμα
                        withContext(Dispatchers.Main) {
                            conversionResult.value = resultString
                            onResultUpdated(resultString) // Notify the UI
                        }
                    } else {
                        // Ενημερώνουμε το UI στο κύριο νήμα
                        withContext(Dispatchers.Main) {
                            conversionResult.value = "Conversion rate not available."
                            onResultUpdated("Conversion rate not available.")
                        }
                    }
                }.onFailure { error ->
                    Log.e("CurrencyViewModel", "Error: ${error.message}")
                    // Ενημερώνουμε το UI στο κύριο νήμα
                    withContext(Dispatchers.Main) {
                        conversionResult.value = "Error: ${error.message}"
                        onResultUpdated("Error: ${error.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("CurrencyViewModel", "Unexpected Error: ${e.message}")
                // Ενημερώνουμε το UI στο κύριο νήμα
                withContext(Dispatchers.Main) {
                    conversionResult.value = "Unexpected Error Occurred."
                    onResultUpdated("Unexpected Error Occurred.")
                }
            } finally {
                // Ενημερώνουμε το UI στο κύριο νήμα
                withContext(Dispatchers.Main) {
                    isLoading.value = false
                }
            }
        }
    }

}
