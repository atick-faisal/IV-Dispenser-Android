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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
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
    var flowPercentage by remember { mutableFloatStateOf(0.5F) }
    var isBottomMenuOpen by rememberSaveable { mutableStateOf(false) }

    val dispenserState by viewModel.lastState.collectAsState()
    val urineLevel by viewModel.urineLevel.collectAsState()
    val dripRateDataset by viewModel.dripRateDataset.collectAsState()
    val flowRateDataset by viewModel.flowRateDataset.collectAsState()
    val urineOutDataset by viewModel.urineOutDataset.collectAsState()
    val sendingCommand by viewModel.sendingCommand

    return Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isBottomMenuOpen) Color.Black else MaterialTheme.colors.background
            )
    ) {
        Column(Modifier.fillMaxSize()) {
            TopBar(
                title = "Room No. ${dispenserState.room ?: "101"}",
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
                dispenserState = dispenserState,
                urineOutDataset = urineOutDataset,
                flowRateDataset = flowRateDataset,
                dripRateDataset = dripRateDataset,
                urineLevel = urineLevel
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
                    isLoaderVisible = sendingCommand,
                    onCancel = { viewModel.commandSent() }
                )
            }
        }
    }
}