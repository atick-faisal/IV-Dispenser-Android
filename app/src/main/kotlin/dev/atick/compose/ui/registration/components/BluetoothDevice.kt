package dev.atick.compose.ui.registration.components

import android.bluetooth.BluetoothDevice
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@ExperimentalAnimationApi
@Composable
fun BluetoothDevice(
    modifier: Modifier = Modifier,
    bluetoothDevice: BluetoothDevice,
    isDeviceConnected: Boolean,
    isRegistrationComplete: Boolean,
    onClick: (BluetoothDevice) -> Unit,
    onSubmitClick: (String) -> Unit
) {
    return Card(
        modifier = modifier.then(
            Modifier.fillMaxWidth()
        ),
        elevation = if (isSystemInDarkTheme()) 0.dp else 2.dp,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = if (isRegistrationComplete) MaterialTheme.colors.primary
        else MaterialTheme.colors.surface
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            DeviceInfo(
                bluetoothDevice = bluetoothDevice,
                isDeviceConnected = isDeviceConnected,
                isRegistrationComplete = isRegistrationComplete,
                onClick = onClick
            )
            AnimatedVisibility(
                visible = (isDeviceConnected && !isRegistrationComplete)
            ) {
                RegistrationForm(onSubmitClick = onSubmitClick)
            }
        }
    }
}