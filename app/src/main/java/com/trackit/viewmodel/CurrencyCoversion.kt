package com.trackit.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackit.repository.api.CurrencyApiHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CurrencyViewModel : ViewModel() {

    private val apiHandler = CurrencyApiHandler()

    private var conversionResult = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    fun fetchExchangeRates(baseCurrency: String, targetCurrency: String, apiKey: String, amount: Double, onResultUpdated: (String) -> Unit) {
        // Use Dispatchers.IO for API call
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.value = true
            val result = apiHandler.getExchangeRates(baseCurrency, apiKey)

            // Use Dispatchers.Default for computation
            val processedResult = withContext(Dispatchers.Default) {
                result.mapCatching { response ->
                    val rate = response.rates?.get(targetCurrency)
                    if (rate != null) {
                        "Result: %.2f $targetCurrency".format(amount * rate)
                    } else {
                        "Conversion rate not available."
                    }
                }
            }

            // Update the UI on the Main thread
            withContext(Dispatchers.Main) {
                processedResult.onSuccess {
                    conversionResult.value = it
                    onResultUpdated(it)
                }.onFailure {
                    conversionResult.value = "Error: ${it.message}"
                    onResultUpdated("Error: ${it.message}")
                }
                isLoading.value = false
            }
        }
    }

}
