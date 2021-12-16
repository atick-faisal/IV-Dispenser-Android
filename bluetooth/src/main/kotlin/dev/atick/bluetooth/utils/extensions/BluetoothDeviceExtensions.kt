package dev.atick.bluetooth.utils.extensions

import android.bluetooth.BluetoothClass.Device.Major.*
import android.bluetooth.BluetoothDevice
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

fun BluetoothDevice.getDeviceIcon(): ImageVector {
    return when (this.bluetoothClass.majorDeviceClass) {
        AUDIO_VIDEO -> Icons.Default.BluetoothAudio
        // COMPUTER -> Icons.Default.Computer // For ESP32
        HEALTH -> Icons.Default.DirectionsRun
        IMAGING -> Icons.Default.Image
        NETWORKING -> Icons.Default.Cloud
        PERIPHERAL -> Icons.Default.Image
        PHONE -> Icons.Default.KeyboardAlt
        TOY -> Icons.Default.Toys
        UNCATEGORIZED -> Icons.Default.Bluetooth
        WEARABLE -> Icons.Default.Watch
        else -> Icons.Default.HealthAndSafety // For ESP32
    }
}