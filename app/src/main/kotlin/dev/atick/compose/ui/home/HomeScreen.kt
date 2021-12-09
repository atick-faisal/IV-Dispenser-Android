package dev.atick.compose.ui.home

import androidx.compose.foundation.clickable
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
fun HomeScreen(
    onDispenserClick: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val dispensers = viewModel.dispensers.observeAsState()

    return Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(dispensers.value ?: listOf()) {
                Text(
                    modifier = Modifier.clickable { onDispenserClick.invoke(it.deviceId) },
                    text = it.deviceId
                )
            }
        }
    }
}