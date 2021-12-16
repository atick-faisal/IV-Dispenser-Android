package dev.atick.compose.ui.registration

import android.bluetooth.BluetoothDevice
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
fun PairedDevicesScreen(
    connectToBTDevice: (BluetoothDevice) -> Unit,
    sendRegistrationInfo: (String) -> Unit,
    viewModel: RegistrationViewModel = viewModel()
) {

    val pairedDevices = viewModel.pairedDevices.observeAsState()
    val connectedDeviceId = viewModel.connectedDeviceId.observeAsState()

    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        TopBar(
            title = "Paired Devices",
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
            items(pairedDevices.value ?: listOf()) {
                BluetoothDevice(
                    bluetoothDevice = it,
                    isDeviceConnected = it.address == connectedDeviceId.value,
                    onClick = connectToBTDevice,
                    onSubmitClick = sendRegistrationInfo
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}