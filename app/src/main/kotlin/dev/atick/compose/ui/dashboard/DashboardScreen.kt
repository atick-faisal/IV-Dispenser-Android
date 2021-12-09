package dev.atick.compose.ui.dashboard

import ai.atick.material.MaterialColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.atick.compose.ui.dashboard.components.TopBar
import dev.atick.compose.ui.dashboard.components.WaterProgress

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel()
) {

    val dispenserState = viewModel.lastState.observeAsState()
    val progress = viewModel.urinePercentage.observeAsState()

    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialColor.BlueGray50),
        horizontalAlignment = CenterHorizontally
    ) {
        TopBar(
            title = "Room No. ${dispenserState.value?.room ?: "101"}"
        )

        Spacer(modifier = Modifier.height(16.dp))

        WaterProgress(
            title = "Urine Out",
            value = dispenserState.value?.urineOut ?: 0F,
            progress = progress.value ?: 0F,
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
        )
    }
}