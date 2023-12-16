package dev.atick.compose.ui.dashboard.components

import android.animation.ValueAnimator
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.github.mikephil.charting.data.LineDataSet
import dev.atick.compose.R
import dev.atick.core.utils.extensions.round
import dev.atick.data.models.DispenserState

@Composable
fun DashboardContent(
    modifier: Modifier = Modifier,
    dispenserState: DispenserState?,
    urineOutDataset: LineDataSet,
    flowRateDataset: LineDataSet,
    dripRateDataset: LineDataSet,
    urineLevel: Float
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(state = scrollState, enabled = true)
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // ----------------------------------- URINE OUT --------------------------------- //

        SensorCard(
            title = "Urine Output",
            value = "${dispenserState?.urineOut?.round() ?: 500F} mL",
            lastUpdated = dispenserState?.timestamp ?: 0,
            lineDataset = urineOutDataset
        ) {
            AndroidView(
                factory = { ctx ->
                    LottieAnimationView(ctx).apply {
                        setAnimation(R.raw.urine_level)
                        repeatCount = ValueAnimator.INFINITE
                        repeatMode = LottieDrawable.REVERSE
                        enableMergePathsForKitKatAndAbove(true)
                        speed = 0.3F
                        playAnimation()
                    }
                },
                update = {
                    it.setMinAndMaxFrame(
                        if (urineLevel <= 0.05F) 0
                        else (150F * (urineLevel - 0.05F)).toInt(),
                        if (urineLevel >= 0.95F) 150
                        else (150F * (urineLevel + 0.05F)).toInt()
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
            value = "${dispenserState?.flowRate?.round() ?: 30} mL/h",
            lastUpdated = dispenserState?.timestamp ?: 0,
            lineDataset = flowRateDataset
        ) {
            AndroidView(
                factory = { ctx ->
                    LottieAnimationView(ctx).apply {
                        setAnimation(R.raw.flow_rate)
                        repeatCount = ValueAnimator.INFINITE
                        enableMergePathsForKitKatAndAbove(true)
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
            value = "${dispenserState?.dripRate?.round() ?: 60F} /min",
            lastUpdated = dispenserState?.timestamp ?: 0,
            lineDataset = dripRateDataset
        ) {
            AndroidView(
                factory = { ctx ->
                    LottieAnimationView(ctx).apply {
                        setAnimation(R.raw.droplet)
                        repeatCount = ValueAnimator.INFINITE
                        enableMergePathsForKitKatAndAbove(true)
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