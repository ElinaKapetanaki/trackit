package com.example.activity_signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

/**
 * Το κύριο Composable (ChartsScreen) που παίρνει το ChartsViewModel
 * και δείχνει τρία γραφήματα σε μία LazyColumn.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsScreen(
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit,
    onChartsClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    onExchangeClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    viewModel: ChartsViewModel = viewModel()
) {
    // Παίρνουμε τη λίστα chartItems από το ViewModel
    val chartItems = viewModel.chartItems

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text("Charts", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                tonalElevation = 4.dp
            ) {
                // Home Icon
                IconButton(onClick = onHomeClick, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.Home, // Replace with a Home icon if available
                        contentDescription = "Home",
                        tint = Color.Black
                    )
                }

                // Charts Icon
                IconButton(onClick = onChartsClick, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = "Charts",
                        tint = Color.Black
                    )
                }

                // "+" Button in the center
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.Black)
                        .align(Alignment.CenterVertically)
                        .clickable { onAddButtonClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }

                // Exchange Icon
                IconButton(onClick = onExchangeClick, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.CurrencyExchange,
                        contentDescription = "Exchange",
                        tint = Color.Black
                    )
                }

                // Edit Profile Icon
                IconButton(onClick = onEditProfileClick, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = Color.Black
                    )
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
        ) {
            // LazyColumn για scroll αν δεν χωράνε
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
 * Μία ενότητα γραφήματος: Τίτλος, περιγραφή, και ένα LineChart
 * σε μαύρο background, ύψος 250dp για να μη γεμίζει την οθόνη.
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
 * To LineChartComposable (MPAndroidChart) που σχεδιάζει λευκή γραμμή σε μαύρο background.
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
                setExtraOffsets(10f, 10f, 10f, 10f)
                setBackgroundColor(android.graphics.Color.BLACK)

                xAxis.apply {
                    textSize = 14f
                    textColor = android.graphics.Color.WHITE
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f
                    valueFormatter = IndexAxisValueFormatter(labels)
                }
                axisLeft.apply {
                    textSize = 14f
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
            // Φτιάχνουμε τα entries από τα data
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
                    textSize = 12f
                    textColor = android.graphics.Color.WHITE
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f
                    valueFormatter = IndexAxisValueFormatter(labels)
                }
                axisLeft.apply {
                    textSize = 14f
                    textColor = android.graphics.Color.WHITE
                    setDrawGridLines(false)
                }
                axisRight.isEnabled = false
                legend.isEnabled = false
            }
        },
        update = { chart ->
            val entries = data.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }
            val dataSet = BarDataSet(entries, "Categories").apply {
                colors = listOf(
                    android.graphics.Color.parseColor("#FFCCCB"), // Food
                    android.graphics.Color.parseColor("#AECBFA"), // Shopping
                    android.graphics.Color.parseColor("#F8BBE8"), // Entertainment
                    android.graphics.Color.parseColor("#B8E4C9"),  // Rent
                    android.graphics.Color.parseColor("#FFD700")  // Other
                )
                valueTextColor = android.graphics.Color.WHITE
                valueTextSize = 12f
            }
            chart.data = BarData(dataSet)
            chart.invalidate()
        }
    )
}





