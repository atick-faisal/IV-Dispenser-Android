package dev.atick.compose.ui.dashboard

import android.animation.ValueAnimator
import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import dev.atick.compose.R
import dev.atick.compose.ui.dashboard.components.Knob
import dev.atick.compose.ui.dashboard.components.LinePlot
import dev.atick.compose.ui.dashboard.components.TopBar
import dev.atick.compose.ui.dashboard.components.WaterProgress
import dev.atick.compose.utils.getFormattedDateTime
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

//        Spacer(modifier = Modifier.height(16.dp))
//
//        Slider(
//            value = sliderValue.value,
//            onValueChange = {
//                sliderValue.value = it
//            },
//            Modifier.pointerInteropFilter { motionEvent ->
//                when (motionEvent.action) {
//                    MotionEvent.ACTION_MOVE -> {
//                        sliderValue.value += 0.5F
//                        true
//                    }
//                    else -> false
//                }
//            }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(state = scrollState, enabled = true),
            horizontalAlignment = CenterHorizontally
        ) {
//            WaterProgress(
//                title = "Urine Out",
//                value = dispenserState.value?.urineOut ?: 0F,
//                progress = progress.value ?: 0F,
//                modifier = Modifier
//                    .width(300.dp)
//                    .height(300.dp)
//            )

            Spacer(modifier = Modifier.height(16.dp))

            // ----------------------------------- URINE OUT --------------------------------- //

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = 0.dp, //if (isSystemInDarkTheme()) 0.dp else 2.dp,
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {

                    Box(
                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Urine Out",
                                fontSize = 16.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "${dispenserState.value?.urineOut?.round() ?: 500F} mL",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Light
                            )
                        }

                        Column(
                            modifier = Modifier.align(TopEnd),
//                            horizontalAlignment = Alignment.End
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
                                    .width(120.dp)
                                    .height(120.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinePlot(
                        dataset = urineOutDataset.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ------------------------------- DRIP RATE ------------------------------- //

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = 0.dp, //if (isSystemInDarkTheme()) 0.dp else 2.dp,
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(0.6F)
                        ) {
                            Text(
                                text = "Drip Rate",
                                fontSize = 16.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "${dispenserState.value?.dripRate?.round() ?: 60F} /min",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Light
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Last Updated: ${
                                    getFormattedDateTime(
                                        dispenserState.value?.timestamp ?: 0
                                    )
                                }",
                                fontSize = 12.sp
                            )
                        }

                        Column(
                            modifier = Modifier.weight(0.4F)
                        ) {
                            AndroidView(
                                factory = { ctx ->
                                    LottieAnimationView(ctx).apply {
                                        setAnimation(R.raw.droplet)
                                        repeatCount = ValueAnimator.INFINITE
                                        playAnimation()
                                    }
                                },
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

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}