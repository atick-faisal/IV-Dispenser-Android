package dev.atick.compose.ui.home

import ai.atick.material.MaterialColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.atick.compose.ui.home.components.DispenserItem
import dev.atick.compose.ui.home.components.TopBar

@Composable
fun HomeScreen(
    onDispenserClick: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val dispensers = viewModel.dispensers.observeAsState()

    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialColor.BlueGray50)
    ) {

        TopBar()

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 8.dp,
                    end = 8.dp
                )
        ) {
            items(dispensers.value ?: listOf()) {
//                Text(
//                    modifier = Modifier.clickable { onDispenserClick.invoke(it.deviceId) },
//                    text = it.deviceId
//                )
                DispenserItem(dispenser = it, onClick = { deviceId ->
                    onDispenserClick.invoke(deviceId)
                })
            }
        }
    }
}