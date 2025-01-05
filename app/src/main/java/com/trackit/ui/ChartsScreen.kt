package com.trackit.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trackit.ui.components.BottomNavBar
import com.trackit.ui.components.PageTopBar
import com.trackit.viewmodel.ChartsViewModel
import com.trackit.viewmodel.ChartItem
import com.trackit.viewmodel.AppViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsScreen(
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit,
    onChartsClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    onExchangeClick: () -> Unit,
    onEditProfileClick: () -> Unit
) {
    // Get the ViewModel with the Factory
    val viewModel: ChartsViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )

    // Fetch chart data from the ViewModel
    viewModel.fetchChartData()

    // Get chart items from the ViewModel
    val chartItems = viewModel.chartItems

    Scaffold(
        topBar = {
            PageTopBar(
                title = "Charts",
                onBackClick = onBackClick
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
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
        ) {
            // LazyColumn for scrollable content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Your Expense Insights",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                items(chartItems) { item ->
                    CategoryChartSection(
                        title = item.title,
                        description = item.description,
                        data = item.data,
                        labels = item.labels
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

/**
 * A section for each chart, with title, description, and either a LineChart or BarChart.
 */
@Composable
fun CategoryChartSection(
    title: String,
    description: String,
    data: List<Float>,
    labels: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            description,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            if (title == "By Category") {
                BarChartComposable(data = data, labels = labels)
            } else {
                LineChartComposable(data = data, labels = labels)
            }
        }
    }
}

/**
 * LineChart Composable using MPAndroidChart to display line chart on a black background.
 */
@Composable
fun LineChartComposable(
    data: List<Float>,
    labels: List<String>
) {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                layoutParams = android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
                )
                description.isEnabled = false
                setDrawGridBackground(false)
                setPinchZoom(false)
                animateX(1500)
                setExtraOffsets(15f, 20f, 15f, 20f) // Added extra offsets for better spacing
                setBackgroundColor(android.graphics.Color.BLACK)

                xAxis.apply {
                    textSize = 16f // Increased text size for axis labels
                    textColor = android.graphics.Color.WHITE
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f
                    valueFormatter = IndexAxisValueFormatter(labels)
                    setLabelCount(labels.size, false)
                }
                axisLeft.apply {
                    textSize = 16f
                    textColor = android.graphics.Color.WHITE
                    setDrawGridLines(false)
                }
                axisRight.isEnabled = false

                legend.apply {
                    textSize = 12f
                    textColor = android.graphics.Color.WHITE
                    horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                    verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                    orientation = Legend.LegendOrientation.HORIZONTAL
                    isWordWrapEnabled = true
                }
            }
        },
        update = { chart ->
            val entries = data.mapIndexed { index, value ->
                Entry(index.toFloat(), value)
            }
            val dataSet = LineDataSet(entries, "Expenses").apply {
                valueTextSize = 0f
                setCircleColor(android.graphics.Color.WHITE)
                color = android.graphics.Color.WHITE
                lineWidth = 2f
                setDrawCircles(true)
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }
            val lineData = LineData(dataSet)
            chart.data = lineData
            chart.invalidate()
        }
    )
}

/**
 * BarChart Composable using MPAndroidChart to display bar chart on a black background.
 */
@Composable
fun BarChartComposable(
    data: List<Float>,
    labels: List<String>
) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                setDrawGridBackground(false)
                setPinchZoom(false)
                animateY(1500)
                setBackgroundColor(android.graphics.Color.BLACK)

                xAxis.apply {
                    textSize = 16f // Increased text size for axis labels
                    textColor = android.graphics.Color.WHITE
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f
                    valueFormatter = IndexAxisValueFormatter(labels)
                }
                axisLeft.apply {
                    textSize = 16f
                    textColor = android.graphics.Color.WHITE
                    setDrawGridLines(false)
                }
                axisRight.isEnabled = false
                legend.isEnabled = false
            }
        },
        update = { chart ->
            val entries = data.mapIndexed { index, value ->
                BarEntry(index.toFloat(), value)
            }
            val dataSet = BarDataSet(entries, "Categories").apply {
                colors = listOf(
                    android.graphics.Color.parseColor("#FFCCCB"), // Food
                    android.graphics.Color.parseColor("#AECBFA"), // Shopping
                    android.graphics.Color.parseColor("#F8BBE8"), // Entertainment
                    android.graphics.Color.parseColor("#B8E4C9"),  // Rent
                    android.graphics.Color.parseColor("#FFD700")  // Other
                )
                valueTextColor = android.graphics.Color.WHITE
                valueTextSize = 16f // Increased text size for values
            }
            chart.data = BarData(dataSet)
            chart.invalidate()
        }
    )
}
