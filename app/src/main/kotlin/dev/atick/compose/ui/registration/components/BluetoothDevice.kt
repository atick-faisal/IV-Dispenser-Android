package dev.atick.compose.ui.registration.components

import android.bluetooth.BluetoothClass.Device.Major.COMPUTER
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.bluetooth.utils.extensions.getDeviceIcon

@Composable
fun BluetoothDevice(
    modifier: Modifier = Modifier,
    bluetoothDevice: BluetoothDevice,
    onClick: (BluetoothDevice) -> Unit
) {
    return Card(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .clickable { onClick.invoke(bluetoothDevice) },
        ),
        elevation = if (isSystemInDarkTheme()) 0.dp else 2.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = bluetoothDevice.getDeviceIcon(),
                contentDescription = "Device type",
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp),
                tint = if (bluetoothDevice.bluetoothClass.majorDeviceClass == COMPUTER) {
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


    }
}