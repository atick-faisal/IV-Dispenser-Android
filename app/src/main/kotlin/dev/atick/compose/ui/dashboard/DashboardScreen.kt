package dev.atick.compose.ui.dashboard

import android.animation.ValueAnimator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.LottieAnimationView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import dev.atick.compose.R
import dev.atick.compose.ui.common.components.BottomMenu
import dev.atick.compose.ui.common.components.TopBar
import dev.atick.compose.ui.dashboard.components.DashboardContent
import dev.atick.compose.ui.dashboard.components.Knob
import dev.atick.core.utils.extensions.decimalFormat
import dev.atick.core.utils.extensions.round

@ExperimentalComposeUiApi
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel()
) {


    var flowPercentage by remember { mutableStateOf(0.5F) }

    val dispenserState = viewModel.lastState.observeAsState()
    val urineLevel = viewModel.urineLevel.observeAsState(0F)
    val dripRateDataset = viewModel.dripRateDataset.observeAsState(
        initial = LineDataSet(
            mutableListOf<Entry>(), ""
        )
    )
    val flowRateDataset = viewModel.flowRateDataset.observeAsState(
        initial = LineDataSet(
            mutableListOf<Entry>(), ""
        )
    )
    val urineOutDataset = viewModel.urineOutDataset.observeAsState(
        initial = LineDataSet(
            mutableListOf<Entry>(), ""
        )
    )

    return Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)

    ) {
        Column(Modifier.fillMaxSize()) {
            TopBar(
                title = "Room No. ${dispenserState.value?.room ?: "101"}"
            )

            DashboardContent(
                modifier = Modifier.alpha(1.0F),
                dispenserState = dispenserState.value,
                urineOutDataset = urineOutDataset.value,
                flowRateDataset = flowRateDataset.value,
                dripRateDataset = dripRateDataset.value,
                urineLevel = urineLevel.value
            )
        }

//        BottomMenu(
//            modifier = Modifier.align(Alignment.BottomCenter)
//        ) {
//            Row(
//                modifier = Modifier.padding(
//                    start = 16.dp,
//                    end = 16.dp,
//                    top = 32.dp,
//                    bottom = 32.dp
//                ),
//                horizontalArrangement = Arrangement.SpaceAround,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Knob(
//                    modifier = Modifier
//                        .width(120.dp)
//                        .height(120.dp),
//                    percent = flowPercentage,
//                    onValueChange = { flowPercentage = it }
//                )
//
//                Column {
//                    Text(
//                        text = "Flow Rate",
//                        fontSize = 20.sp
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(
//                        text = "${flowPercentage.round()} mL/h",
//                        fontSize = 32.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    AndroidView(
//                        factory = { ctx ->
//                            LottieAnimationView(ctx).apply {
//                                setAnimation(R.raw.loader)
//                                repeatCount = ValueAnimator.INFINITE
//                                playAnimation()
//                            }
//                        },
//                        modifier = Modifier
//                            .height(20.dp)
//                            .width(100.dp)
//                    )
//                }
//            }
//        }
    }
}