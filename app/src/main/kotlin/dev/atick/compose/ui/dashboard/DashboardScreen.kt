package dev.atick.compose.ui.dashboard

import android.animation.ValueAnimator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import dev.atick.compose.R
import dev.atick.compose.ui.common.components.TopBar
import dev.atick.compose.ui.dashboard.components.SensorCard
import dev.atick.compose.utils.round

@ExperimentalComposeUiApi
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel()
) {

    val scrollState = rememberScrollState()
    val sliderValue = remember { mutableStateOf(0.5F) }

    val dispenserState = viewModel.lastState.observeAsState()
    val progress = viewModel.urinePercentage.observeAsState(0F)
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

    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)

    ) {
        TopBar(
            title = "Room No. ${dispenserState.value?.room ?: "101"}"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(state = scrollState, enabled = true),
            horizontalAlignment = CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ----------------------------------- URINE OUT --------------------------------- //

            SensorCard(
                title = "Urine Output",
                value = "${dispenserState.value?.urineOut?.round() ?: 500F} mL",
                lastUpdated = dispenserState.value?.timestamp ?: 0,
                lineDataset = urineOutDataset.value
            ) {
                AndroidView(
                    factory = { ctx ->
                        LottieAnimationView(ctx).apply {
                            setAnimation(R.raw.progress)
                            repeatCount = ValueAnimator.INFINITE
                            repeatMode = LottieDrawable.REVERSE
                            speed = 0.3F
                            playAnimation()
                        }
                    },
                    update = {
                        it.setMinAndMaxFrame(
                            if (progress.value <= 0.05F) 0
                            else (150F * (progress.value - 0.05F)).toInt(),
                            if (progress.value >= 0.95F) 150
                            else (150F * (progress.value + 0.05F)).toInt()
                        )
                    },
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // --------------------------- FLOW RATE --------------------------- //

            SensorCard(
                title = "Flow Rate",
                value = "${dispenserState.value?.flowRate?.round() ?: 30} mL/h",
                lastUpdated = dispenserState.value?.timestamp ?: 0,
                lineDataset = flowRateDataset.value
            ) {
                AndroidView(
                    factory = { ctx ->
                        LottieAnimationView(ctx).apply {
                            setAnimation(R.raw.flow_rate)
                            repeatCount = ValueAnimator.INFINITE
                            speed = 0.2F
                            playAnimation()
                        }
                    },
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ------------------------------- DRIP RATE ------------------------------- //

            SensorCard(
                title = "Drip Rate",
                value = "${dispenserState.value?.dripRate?.round() ?: 60F} /min",
                lastUpdated = dispenserState.value?.timestamp ?: 0,
                lineDataset = dripRateDataset.value
            ) {
                AndroidView(
                    factory = { ctx ->
                        LottieAnimationView(ctx).apply {
                            setAnimation(R.raw.droplet)
                            repeatCount = ValueAnimator.INFINITE
                            playAnimation()
                        }
                    },
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}