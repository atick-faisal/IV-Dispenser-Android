package dev.atick.compose.ui.dashboard.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.mikephil.charting.data.LineDataSet
import dev.atick.compose.utils.getFormattedDateTime

@Composable
fun SensorCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    lastUpdated: Long,
    lineDataset: LineDataSet,
    animatedIcon: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp, //if (isSystemInDarkTheme()) 0.dp else 2.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = value,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Last Updated: ${
                            getFormattedDateTime(
                                lastUpdated
                            )
                        }",
                        fontSize = 12.sp
                    )
                }

                Column(
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    animatedIcon.invoke()
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinePlot(
                dataset = lineDataset,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(8.dp)
            )
        }
    }
}