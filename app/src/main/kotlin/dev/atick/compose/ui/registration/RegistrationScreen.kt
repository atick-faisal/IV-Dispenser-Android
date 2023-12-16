package dev.atick.compose.ui.registration

import androidx.compose.animation.ExperimentalAnimationApi
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
import dev.atick.compose.ui.registration.components.BluetoothDevice

@ExperimentalAnimationApi
@Composable
fun RegistrationScreen(
    onExitClick: () -> Unit,
    viewModel: RegistrationViewModel = viewModel()
) {

    val pairedDevices = viewModel.pairedDevices.observeAsState()
    val connectedDeviceId = viewModel.connectedDeviceId.observeAsState()
    val isRegistrationComplete = viewModel.isRegistrationComplete.observeAsState(false)

    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        TopBar(
            title = "Dispenser Registration",
            onRefreshClick = { viewModel.fetchPairedDevices() },
            onExitClick = onExitClick
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
            items(pairedDevices.value ?: listOf()) {
                BluetoothDevice(
                    bluetoothDevice = it,
                    isDeviceConnected = it.address == connectedDeviceId.value,
                    isRegistrationComplete =
                    it.address == connectedDeviceId.value && isRegistrationComplete.value,
                    onClick = { bluetoothDevice ->
                        viewModel.connectToBTDevice(bluetoothDevice)
                    },
                    onSubmitClick = { registrationInfo ->
                        viewModel.registerDevice(registrationInfo)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}