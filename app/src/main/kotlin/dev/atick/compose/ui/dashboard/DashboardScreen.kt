package dev.atick.compose.ui.dashboard

import android.animation.ValueAnimator
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
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
import dev.atick.core.utils.extensions.round

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel()
) {


    var flowPercentage by remember { mutableStateOf(0.5F) }
    var isBottomMenuOpen by remember { mutableStateOf(false) }

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
    val sendingCommand = viewModel.sendingCommand.observeAsState(false)

    return Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)

    ) {
        Column(Modifier.fillMaxSize()) {
            TopBar(
                title = "Room No. ${dispenserState.value?.room ?: "101"}",
                onMenuClick = { isBottomMenuOpen = true }
            )

            DashboardContent(
                modifier = Modifier
                    .alpha(
                        if (isBottomMenuOpen) 0.2F else 1.0F
                    )
                    .clickable(enabled = isBottomMenuOpen) {
                        isBottomMenuOpen = false
                    },
                dispenserState = dispenserState.value,
                urineOutDataset = urineOutDataset.value,
                flowRateDataset = flowRateDataset.value,
                dripRateDataset = dripRateDataset.value,
                urineLevel = urineLevel.value
            )
        }

        AnimatedVisibility(
            visible = isBottomMenuOpen,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            BottomMenu {
                Row(
                    modifier = Modifier.padding(
                        top = 32.dp,
                        bottom = 64.dp,
                        start = 32.dp,
                        end = 32.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Knob(
                        modifier = Modifier
                            .width(120.dp)
                            .height(120.dp),
                        percent = flowPercentage,
                        onValueChange = { flowPercentage = it },
                        onFinalValue = { viewModel.setFlowRate(it) }
                    )

                    Spacer(modifier = Modifier.width(32.dp))

                    Column(Modifier.fillMaxWidth()) {
                        Text(
                            text = "Flow Rate",
                            fontSize = 20.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${flowPercentage.round()} mL/h",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        AnimatedVisibility(sendingCommand.value) {
                            Column {
                                AndroidView(
                                    factory = { ctx ->
                                        LottieAnimationView(ctx).apply {
                                            setAnimation(R.raw.loader)
                                            repeatCount = ValueAnimator.INFINITE
                                            enableMergePathsForKitKatAndAbove(true)
                                            playAnimation()
                                        }
                                    },
                                    modifier = Modifier
                                        .height(32.dp)
                                        .fillMaxWidth()
                                ) 
                                
                                OutlinedButton(onClick = { viewModel.commandSent() }) {
                                    Text(text = "Cancel")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}