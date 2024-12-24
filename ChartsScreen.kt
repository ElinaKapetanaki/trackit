import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

/**
 * Το ViewModel: Κρατάει την κατάσταση (state) για τα 3 γραφήματα:
 * Weekly, Monthly, By Category. Εδώ τα έχουμε ως απλά στατικά δεδομένα
 * αλλά θα μπορούσες να τα φορτώνεις από Repository / API κ.λπ.
 */
class ChartsViewModel : ViewModel() {

    // Weekly data
    val weeklyData = listOf(120f, 80f, 150f, 60f, 100f, 30f, 100f)
    val weeklyLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    // Monthly data
    val monthlyData = listOf(400f, 250f, 300f, 450f)
    val monthlyLabels = listOf("Week1", "Week2", "Week3", "Week4")

    // Category data
    val categoryData = listOf(200f, 150f, 80f, 120f, 60f)
    val categoryLabels = listOf("Food", "Rent", "Bills", "Fun", "Other")

    /**
     * Φτιάχνουμε μια λίστα “ChartItem” που θα την “διαβάσει” το Compose
     * για να εμφανίσει 3 ενότητες στο LazyColumn.
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
 * Δομή δεδομένων για το κάθε γράφημα (τίτλος, περιγραφή, data, labels).
 */
data class ChartItem(
    val title: String,
    val description: String,
    val data: List<Float>,
    val labels: List<String>
)

/**
 * Το κύριο Composable (ChartsScreen) που παίρνει το ChartsViewModel
 * και δείχνει τρία γραφήματα σε μία LazyColumn.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsScreen(
    onBackClick: () -> Unit = {},
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
        }
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
                .height(250.dp) // Λιγότερο ύψος για να χωράνε όλα στην οθόνη
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            LineChartComposable(
                data = data,
                labels = labels
            )
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

/**
 * Preview (συνήθως “στριμωγμένο”), αλλά βλέπεις μια γενική ιδέα.
 * Για οριστικό layout, τρέξε σε emulator / συσκευή.
 */
@Preview(showBackground = true)
@Composable
fun ChartsScreenPreview() {
    ChartsScreen()
}

