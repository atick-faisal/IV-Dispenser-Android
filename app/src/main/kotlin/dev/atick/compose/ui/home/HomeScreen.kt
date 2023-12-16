package dev.atick.compose.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.atick.compose.ui.common.components.TopBar
import dev.atick.compose.ui.home.components.DispenserItem

@Composable
fun HomeScreen(
    onDispenserClick: (String) -> Unit,
    onAddDispenserClick: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val dispensers by viewModel.dispensers.collectAsState()

    return Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
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
                items(dispensers) {
                    DispenserItem(dispenser = it, onClick = { deviceId ->
                        onDispenserClick.invoke(deviceId)
                    })
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        ExtendedFloatingActionButton(
            onClick = onAddDispenserClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add New Dispenser"
                )
            },
            text = {
                   Text(text = "New Dispenser")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    bottom = 32.dp,
                    end = 16.dp
                )
        )
    }
}