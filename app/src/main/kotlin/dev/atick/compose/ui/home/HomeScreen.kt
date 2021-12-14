package dev.atick.compose.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.atick.compose.ui.common.components.TopBar
import dev.atick.compose.ui.home.components.DispenserItem

@Composable
fun HomeScreen(
    onDispenserClick: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val dispensers = viewModel.dispensers.observeAsState()

    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        TopBar(
            title = "I/V Dispensers",
            onSearchClick = {},
            onMenuClick = {}
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            items(dispensers.value ?: listOf()) {
                DispenserItem(dispenser = it, onClick = { deviceId ->
                    onDispenserClick.invoke(deviceId)
                })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}