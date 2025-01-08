package com.trackit.repository

import android.util.Log
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException

// Response model
data class ExchangeRatesResponse(
    val success: Boolean,           // Indicates if the API request was successful
    val base: String?,              // The base currency
    val date: String?,              // The date of the rates
    val rates: Map<String, Double>? // Map of exchange rates
)

// Define the API interface
interface CurrencyApiService {
    @GET("latest")
    suspend fun getExchangeRates(
        @Query("base") baseCurrency: String,
        @Query("access_key") apiKey: String // Update the query parameter to "access_key"
    ): ExchangeRatesResponse
}

// Retrofit instance
object CurrencyApi {
    private const val BASE_URL = "https://api.exchangeratesapi.io/v1/" // Update to the correct URL

    val service: CurrencyApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApiService::class.java)
    }
}

// API Handler class
class CurrencyApiHandler {

    private val api = CurrencyApi.service

    /**
     * Fetch exchange rates and handle errors gracefully.
     */
    suspend fun getExchangeRates(baseCurrency: String, apiKey: String): Result<ExchangeRatesResponse> {
        Log.d("CurrencyApiHandler", "Request URL: https://api.exchangeratesapi.io/v1/latest?base=$baseCurrency&access_key=$apiKey")

        return try {
            // Log API request details
            Log.d("CurrencyApiHandler", "Requesting exchange rates for base: $baseCurrency")

            // Make API call
            val response = api.getExchangeRates(baseCurrency, apiKey)

            // Validate response
            if (response.success && response.rates != null && response.base != null) {
                Log.d("CurrencyApiHandler", "Received valid response: $response")
                Result.success(response)
            } else {
                Log.e("CurrencyApiHandler", "Invalid response: ${response.success}")
                Result.failure(Exception("Invalid response from API"))
            }
        } catch (e: HttpException) {
            Log.e("CurrencyApiHandler", "HTTP Error: ${e.code()} - ${e.message()}")
            Result.failure(Exception("HTTP Error: ${e.code()} - ${e.message()}"))
        } catch (e: IOException) {
            Log.e("CurrencyApiHandler", "Network Error: ${e.message}")
            Result.failure(Exception("Network Error: ${e.message}"))
        } catch (e: Exception) {
            Log.e("CurrencyApiHandler", "Unexpected Error: ${e.message}")
            Result.failure(Exception("Unexpected Error: ${e.message}"))
        }
    }
}

