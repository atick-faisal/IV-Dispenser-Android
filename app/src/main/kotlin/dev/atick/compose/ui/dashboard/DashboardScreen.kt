package dev.atick.compose.ui.dashboard

import ai.atick.material.MaterialColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.atick.compose.ui.dashboard.components.TopBar

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel()
) {
    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialColor.BlueGray50)
    ) {
        TopBar(
            title = "Room No. ${viewModel.lastState.value.room ?: "101"}"
        )
    }
}