package dev.atick.bluetooth.utils.extensions

import android.annotation.SuppressLint
import android.bluetooth.BluetoothClass.Device.Major.AUDIO_VIDEO
import android.bluetooth.BluetoothClass.Device.Major.HEALTH
import android.bluetooth.BluetoothClass.Device.Major.IMAGING
import android.bluetooth.BluetoothClass.Device.Major.NETWORKING
import android.bluetooth.BluetoothClass.Device.Major.PERIPHERAL
import android.bluetooth.BluetoothClass.Device.Major.PHONE
import android.bluetooth.BluetoothClass.Device.Major.TOY
import android.bluetooth.BluetoothClass.Device.Major.UNCATEGORIZED
import android.bluetooth.BluetoothClass.Device.Major.WEARABLE
import android.bluetooth.BluetoothDevice
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothAudio
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardAlt
import androidx.compose.material.icons.filled.Toys
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.outlined.ViewInAr
import androidx.compose.ui.graphics.vector.ImageVector

@SuppressLint("MissingPermission")
fun BluetoothDevice.getDeviceIcon(): ImageVector {
    return when (this.bluetoothClass.majorDeviceClass) {
        AUDIO_VIDEO -> Icons.Default.BluetoothAudio
//        COMPUTER -> Icons.Default.Computer
        HEALTH -> Icons.AutoMirrored.Filled.DirectionsRun
        IMAGING -> Icons.Default.Image
        NETWORKING -> Icons.Default.Cloud
        PERIPHERAL -> Icons.Default.Image
        PHONE -> Icons.Default.KeyboardAlt
        TOY -> Icons.Default.Toys
        UNCATEGORIZED -> Icons.Default.Bluetooth
        WEARABLE -> Icons.Default.Watch
        else -> Icons.Outlined.ViewInAr
    }
}