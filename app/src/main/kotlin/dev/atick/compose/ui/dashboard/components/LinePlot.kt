package dev.atick.compose.ui.dashboard.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dev.atick.compose.R
import dev.atick.compose.utils.getFormattedDateTime

@Composable
fun LinePlot(
    dataset: LineDataSet,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { ctx ->
            LineChart(ctx).apply {
                description.text = ""
                axisLeft.setDrawLabels(false)
                axisLeft.isEnabled = true
                axisLeft.setDrawGridLines(false)
                axisLeft.axisLineWidth = 3.0F
                axisLeft.axisLineColor = R.color.dark_gray
                axisLeft.setDrawLabels(true)
                axisLeft.zeroLineColor = R.color.dark_gray
                axisLeft.textColor = R.color.dark_gray
                axisLeft.labelCount = 5
                axisRight.setDrawLabels(false)
                axisRight.isEnabled = false
                xAxis.setDrawLabels(true)
                xAxis.isEnabled = true
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.axisLineWidth = 3.0F
                xAxis.textColor = R.color.dark_gray
                xAxis.labelCount = 5
                xAxis.valueFormatter = object : IndexAxisValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return getFormattedDateTime(value)
                    }
                }
                legend.isEnabled = false
                setTouchEnabled(false)
            }
        },
        update = { lineChart ->
            dataset.apply {
                color = R.color.purple_500
                setDrawValues(false)
                setDrawFilled(true)
                setDrawCircleHole(false)
                setDrawCircles(false)
                lineWidth = 3.0F
            }
            val lineData = LineData(dataset)
            lineData.notifyDataChanged()
            lineChart.data = lineData
            lineChart.invalidate()
        },
        modifier = modifier
    )
}