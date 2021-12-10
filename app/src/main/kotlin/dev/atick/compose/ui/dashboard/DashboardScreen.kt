package dev.atick.compose.ui.dashboard

import ai.atick.material.MaterialColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import dev.atick.compose.R
import dev.atick.compose.ui.dashboard.components.LinePlot
import dev.atick.compose.ui.dashboard.components.TopBar
import dev.atick.compose.ui.dashboard.components.WaterProgress
import dev.atick.compose.utils.round

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel()
) {

    val dispenserState = viewModel.lastState.observeAsState()
    val progress = viewModel.urinePercentage.observeAsState()
    val dripRateDataset = viewModel.dripRateDataset.observeAsState(
        initial = LineDataSet(
            mutableListOf<Entry>(), ""
        )
    )

    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialColor.BlueGray50)
    ) {
        TopBar(
            title = "Room No. ${dispenserState.value?.room ?: "101"}"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            WaterProgress(
                title = "Urine Out",
                value = dispenserState.value?.urineOut ?: 0F,
                progress = progress.value ?: 0F,
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(0.75F)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Urine Out",
                                color = Color.Gray,
                                fontSize = 20.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "${dispenserState.value?.dripRate?.round() ?: 60F} /min",
                                color = Color.DarkGray,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Light
                            )
                        }

                        Column(
                            modifier = Modifier.weight(0.25F)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.saline),
                                contentDescription = "",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinePlot(
                        dataset = dripRateDataset.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}