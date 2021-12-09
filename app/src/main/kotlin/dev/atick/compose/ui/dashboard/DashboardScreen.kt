package dev.atick.compose.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel()
) {
    val dispenserStates = viewModel.dispenserStates.observeAsState()

    return Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(dispenserStates.value ?: listOf()) {
                Text(text = it.flowRate.toString())
            }
        }
    }
}