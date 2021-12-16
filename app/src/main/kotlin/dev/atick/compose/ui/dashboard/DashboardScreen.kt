package dev.atick.compose.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import dev.atick.compose.ui.common.components.BottomMenu
import dev.atick.compose.ui.common.components.TopBar
import dev.atick.compose.ui.dashboard.components.BottomMenuContent
import dev.atick.compose.ui.dashboard.components.DashboardContent

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
            .background(
                if (isBottomMenuOpen) Color.Black else MaterialTheme.colors.background
            )
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
                BottomMenuContent(
                    flowPercentage = flowPercentage,
                    onValueChange = { flowPercentage = it },
                    onFinalValueChange = { viewModel.setFlowRate(it) },
                    isLoaderVisible = sendingCommand.value,
                    onCancel = { viewModel.commandSent() }
                )
            }
        }
    }
}