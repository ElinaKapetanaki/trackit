package com.trackit.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackit.repository.AppRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ChartsViewModel(
    private val repository: AppRepository,
    private val userSessionViewModel: UserSessionViewModel
) : ViewModel() {

    private val _weeklyData = mutableListOf<Float>()
    val weeklyData: List<Float> get() = _weeklyData

    private val _weeklyLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val weeklyLabels: List<String> get() = _weeklyLabels

    private val _monthlyData = mutableListOf<Float>()
    val monthlyData: List<Float> get() = _monthlyData

    @RequiresApi(Build.VERSION_CODES.O)
    private val _monthlyLabels = List(30) { index ->
        val today = LocalDate.now()
        today.minusDays((29 - index).toLong()).format(DateTimeFormatter.ofPattern("dd/MM"))
    }
    val monthlyLabels: List<String> get() = _monthlyLabels

    private val _categoryData = mutableListOf<Float>()
    val categoryData: List<Float> get() = _categoryData

    private val _categoryLabels = listOf("Food", "Shopping", "Fun", "Rent", "Other")
    val categoryLabels: List<String> get() = _categoryLabels

    fun fetchChartData() {
        viewModelScope.launch {
            val userId = userSessionViewModel.userId.value
            if (userId != null) {
                val weeklyExpenses = repository.getWeeklyExpensesForUser(userId)
                _weeklyData.clear()
                _weeklyData.addAll(weeklyExpenses.map { it.amount.toFloat() })

                val monthlyExpenses = repository.getMonthlyExpensesForUser(userId)
                _monthlyData.clear()
                _monthlyData.addAll(monthlyExpenses.map { it.amount.toFloat() })

                _categoryData.clear()
                _categoryData.addAll(
                    _categoryLabels.mapIndexed { _, category ->
                        val categoryExpenses = repository.getCategoryExpensesForUser(userId, category)
                        categoryExpenses.sumByDouble { it.amount }.toFloat()
                    }
                )
            }
        }
    }

    val chartItems: List<ChartItem>
        @RequiresApi(Build.VERSION_CODES.O)
        get() = listOf(
            ChartItem("This Week's Expenses", "Track how your daily spending sums up over the week.", weeklyData, weeklyLabels),
            ChartItem("Monthly Overview", "Get a broader view of your expenses this month.", monthlyData, monthlyLabels),
            ChartItem("By Category", "See which categories consume most of your spending.", categoryData, categoryLabels)
        )
}

data class ChartItem(
    val title: String,
    val description: String,
    val data: List<Float>,
    val labels: List<String>
)
