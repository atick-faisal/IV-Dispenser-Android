package dev.atick.compose.ui.dashboard.components

import android.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.utils.Utils
import dev.atick.compose.R
import dev.atick.compose.utils.getFormattedDateTime

@Composable
fun LinePlot(
    dataset: LineDataSet<Nothing>,
    modifier: Modifier = Modifier
) {

    val isDarkThemeEnabled = isSystemInDarkTheme()
    val context = LocalContext.current
    val primaryColor = colorResource(R.color.primary).toArgb()

    AndroidView(
        factory = { ctx ->
            Utils.init(ctx)
            LineChart(ctx).apply {
                description.text = ""
                axisLeft.isEnabled = true
                axisLeft.isDrawGridLinesEnabled = false
                axisLeft.axisLineWidth = 2.0F
                axisLeft.isDrawLabelsEnabled = true
                axisLeft.labelCount = 4
                axisRight.isDrawLabelsEnabled = false
                axisRight.isEnabled = false
                xAxis.isDrawLabelsEnabled = true
                xAxis.isEnabled = true
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.isDrawGridLinesEnabled = false
                xAxis.axisLineWidth = 2.0F
                xAxis.valueFormatter = IAxisValueFormatter { value, _ ->
                    getFormattedDateTime(value)
                }
                legend.isEnabled = false
                isTouchEnabled = false
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
                color = primaryColor
                if (isDarkThemeEnabled) {
                    this.fillColor = primaryColor
                    this.fillAlpha = 40
                } else {
                    this.fillDrawable = ContextCompat.getDrawable(
                        context,
                        R.drawable.green_gradient
                    )
                }
                isDrawValuesEnabled = false
                isDrawFilledEnabled = true
                isDrawCircleHoleEnabled = false
                isDrawCirclesEnabled = false
                lineWidth = 3.0F
            }
            lineChart.data = LineData(dataset)
        },
        modifier = modifier
    )
}
