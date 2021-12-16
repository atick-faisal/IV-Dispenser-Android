package dev.atick.compose.ui.registration.components

import android.bluetooth.BluetoothClass.Device.Major.COMPUTER
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.bluetooth.utils.extensions.getDeviceIcon

@Composable
fun DeviceInfo(
    modifier: Modifier = Modifier,
    bluetoothDevice: BluetoothDevice,
    isDeviceConnected: Boolean,
    onClick: (BluetoothDevice) -> Unit
) {
    Row(
        modifier
            .fillMaxSize()
            .padding(24.dp)
            .clickable { onClick(bluetoothDevice) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = bluetoothDevice.getDeviceIcon(),
                contentDescription = "Device type",
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp),
                tint = if (
                    bluetoothDevice.bluetoothClass.majorDeviceClass ==  COMPUTER) {
                    MaterialTheme.colors.primary
                } else MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.width(24.dp))

            Column {
                Text(
                    text = bluetoothDevice.name,
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = bluetoothDevice.address,
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 16.sp,
                )
            }
        }

        Icon(
            imageVector = if (
                bluetoothDevice.bluetoothClass.majorDeviceClass == COMPUTER
            ) {
                if (isDeviceConnected) {
                    Icons.Default.ArrowDropDown
                } else {
                    Icons.Default.NavigateNext
                }

            } else Icons.Default.Error,
            contentDescription = "Connect"
        )
    }
}