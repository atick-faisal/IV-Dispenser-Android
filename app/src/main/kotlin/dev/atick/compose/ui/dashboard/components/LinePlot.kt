package dev.atick.compose.ui.dashboard.components

import ai.atick.material.MaterialColor
import android.graphics.Color
import android.graphics.ColorFilter
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
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

    val isDarkThemeEnabled = isSystemInDarkTheme()
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            LineChart(ctx).apply {
                description.text = ""
                axisLeft.setDrawLabels(false)
                axisLeft.isEnabled = true
                axisLeft.setDrawGridLines(false)
                axisLeft.axisLineWidth = 2.0F
                axisLeft.setDrawLabels(true)
                axisLeft.labelCount = 4
                axisRight.setDrawLabels(false)
                axisRight.isEnabled = false
                xAxis.setDrawLabels(true)
                xAxis.isEnabled = true
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.axisLineWidth = 2.0F
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
            lineChart.xAxis.labelCount = 3
            lineChart.axisLeft.textColor = if (isDarkThemeEnabled) {
                Color.LTGRAY
            } else {
                Color.DKGRAY
            }
            lineChart.xAxis.textColor = if (isDarkThemeEnabled) {
                Color.LTGRAY
            } else {
                Color.DKGRAY
            }
            dataset.apply {
                color = context.getColor(R.color.primary)
                if (isDarkThemeEnabled) {
                    this.fillColor = context.getColor(R.color.primary)
                    this.fillAlpha = 40
                } else {
                    this.fillDrawable = ContextCompat.getDrawable(
                        context,
                        R.drawable.green_gradient
                    )
                }
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