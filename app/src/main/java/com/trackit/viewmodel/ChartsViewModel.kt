package com.trackit.viewmodel
import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * ViewModel: Κρατάει την κατάσταση για τα 3 γραφήματα: Weekly, Monthly, By Category.
 */
class ChartsViewModel : ViewModel() {

    // Weekly data
    val weeklyData = listOf(120f, 80f, 150f, 60f, 100f, 30f, 100f)
    val weeklyLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    // Monthly data
    val monthlyData = List(30) { (1..100).random().toFloat() } // Mock data για τις ημέρες
    @SuppressLint("NewApi")
    val monthlyLabels = List(30) { index ->
        val today = LocalDate.now()
        today.minusDays((29 - index).toLong()).format(DateTimeFormatter.ofPattern("dd/MM"))
    }


    // Category data
    val categoryData = listOf(300f, 50f, 50f, 500f, 150f)
    val categoryLabels = listOf("Food", "Shopping", "Fun", "Rent", "Other")

    /**
     * Λίστα με δεδομένα για το κάθε γράφημα.
     */
    val chartItems = listOf(
        ChartItem(
            title = "This Week's Expenses",
            description = "Track how your daily spending sums up over the week.",
            data = weeklyData,
            labels = weeklyLabels
        ),
        ChartItem(
            title = "Monthly Overview",
            description = "Get a broader view of your expenses this month.",
            data = monthlyData,
            labels = monthlyLabels
        ),
        ChartItem(
            title = "By Category",
            description = "See which categories consume most of your spending.",
            data = categoryData,
            labels = categoryLabels
        )
    )
}

/**
 * Δομή για τα δεδομένα γραφημάτων.
 */
data class ChartItem(
    val title: String,
    val description: String,
    val data: List<Float>,
    val labels: List<String>
)
