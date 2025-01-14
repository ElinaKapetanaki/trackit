package com.trackit.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackit.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@SuppressLint("NewApi")
class ChartsViewModel(
    private val repository: AppRepository,
    private val userSessionViewModel: UserSessionViewModel
) : ViewModel() {

    @SuppressLint("NewApi")
    private val _weeklyLabels = MutableStateFlow<List<String>>(generateWeeklyLabels())
    val weeklyLabels: StateFlow<List<String>> = _weeklyLabels

    private val _weeklyData = MutableStateFlow<List<Float>>(List(7) { 0f }) // Placeholder data
    val weeklyData: StateFlow<List<Float>> = _weeklyData

    @SuppressLint("NewApi")
    private val _monthlyLabels = MutableStateFlow<List<String>>(generateMonthlyLabels())
    val monthlyLabels: StateFlow<List<String>> = _monthlyLabels

    private val _monthlyData = MutableStateFlow<List<Float>>(List(_monthlyLabels.value.size) { 0f }) // Placeholder data
    val monthlyData: StateFlow<List<Float>> = _monthlyData

    private val _categoryData = MutableStateFlow<List<Float>>(emptyList())
    val categoryData: StateFlow<List<Float>> = _categoryData

    private val _categoryLabels = listOf("Food", "Supermarket", "Shopping", "Fun", "Others")
    val categoryLabels: List<String> get() = _categoryLabels

    private val _currentMonthTitle = MutableStateFlow("")
    val currentMonthTitle: StateFlow<String> = _currentMonthTitle

    @RequiresApi(Build.VERSION_CODES.O)
    private fun computeCurrentMonthTitle(): String {
        val today = LocalDate.now()
        return today.format(DateTimeFormatter.ofPattern("MMMM yyyy")) // e.g., "January 2025"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCurrentMonthTitle() {
        _currentMonthTitle.value = computeCurrentMonthTitle()
    }



    init {
        fetchChartData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateWeeklyLabels(): List<String> {
        val today = LocalDate.now()
        val weekStart = today.minusDays(6)
        return (0..6).map { weekStart.plusDays(it.toLong()).format(DateTimeFormatter.ofPattern("EEE")) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateMonthlyLabels(): List<String> {
        val today = LocalDate.now()
        val monthStart = today.withDayOfMonth(1)
        val monthDates = (0 until today.lengthOfMonth()).map { monthStart.plusDays(it.toLong()) }
        return monthDates.filterIndexed { index, _ -> index % 5 == 0 }
            .map { it.format(DateTimeFormatter.ofPattern("d/M")) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchChartData() {
        viewModelScope.launch {
            val userId = userSessionViewModel.userId.value
            if (userId != null) {
                updateCurrentMonthTitle()
                fetchWeeklyExpenses(userId)
                fetchMonthlyExpenses(userId)
                fetchCategoryExpenses(userId)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun fetchWeeklyExpenses(userId: Int) {
        val today = LocalDate.now()
        val weekStart = today.minusDays(6)
        val weekDates = (0..6).map { weekStart.plusDays(it.toLong()) }

        // Fetch weekly expenses
        val weeklyExpenses = repository.getExpensesForUserBetweenDates(
            userId,
            weekStart.toString(),
            today.toString()
        )

        _weeklyData.value = weekDates.map { date ->
            weeklyExpenses
                .filter { it.date == date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
                .sumOf { it.amount.toDouble() } // Use Double to ensure precision
                .toFloat() // Convert to Float for StateFlow
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun fetchMonthlyExpenses(userId: Int) {
        val today = LocalDate.now()
        val monthStart = today.withDayOfMonth(1)
        val monthDates = (0 until today.lengthOfMonth()).map { monthStart.plusDays(it.toLong()) }

        // Fetch monthly expenses
        val monthlyExpenses = repository.getExpensesForUserBetweenDates(
            userId,
            monthStart.toString(),
            today.toString()
        )

        _monthlyData.value = monthDates.chunked(5).map { chunk ->
            chunk.sumOf { date ->
                monthlyExpenses
                    .filter { it.date == date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
                    .sumOf { it.amount.toDouble() } // Use Double to ensure precision
            }.toFloat() // Convert to Float for StateFlow
        }
    }


    @SuppressLint("NewApi")
    private suspend fun fetchCategoryExpenses(userId: Int) {
        val today = LocalDate.now()
        val monthStart = today.withDayOfMonth(1)

        val categoryExpenses = repository.getExpensesForUserBetweenDates(
            userId,
            monthStart.toString(),
            today.toString()
        )

        _categoryData.value = _categoryLabels.map { category ->
            categoryExpenses
                .filter { it.category == category }
                .sumOf { it.amount.toDouble() } // Use Double for summation precision
                .toFloat() // Convert to Float after summation
        }

    }

    val chartItems: List<ChartItem>
        @RequiresApi(Build.VERSION_CODES.O)
        get() = listOf(
            ChartItem(
                "This Week's Expenses",
                "Track how your daily spending sums up over the week.",
                weeklyData.value,
                weeklyLabels.value
            ),
            ChartItem(
                "This Month's Expenses",
                "View how much you spend till now in your current month.",
                monthlyData.value,
                monthlyLabels.value
            ),
            ChartItem(
                "By Category",
                "See which categories consume most of your spending.",
                categoryData.value,
                categoryLabels
            )
        )
}

data class ChartItem(
    val title: String,
    val description: String,
    val data: List<Float>,
    val labels: List<String>
)
