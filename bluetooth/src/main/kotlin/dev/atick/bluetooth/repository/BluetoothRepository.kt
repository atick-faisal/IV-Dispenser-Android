package dev.atick.bluetooth.repository

import androidx.activity.ComponentActivity
import dev.atick.bluetooth.models.BluetoothDevice

interface BluetoothRepository {
    fun isBluetoothAvailable(): Boolean
    fun enableBluetooth(activity: ComponentActivity, onActivityResult: () -> Unit)
    fun getPairedDevicesList(): List<BluetoothDevice>
}