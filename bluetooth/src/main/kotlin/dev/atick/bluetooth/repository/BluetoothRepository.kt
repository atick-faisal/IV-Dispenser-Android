package dev.atick.bluetooth.repository

import android.bluetooth.BluetoothDevice
import androidx.activity.ComponentActivity

interface BluetoothRepository {
    fun isBluetoothAvailable(): Boolean
    fun enableBluetooth(activity: ComponentActivity, onActivityResult: () -> Unit)
    fun getPairedDevicesList(): List<BluetoothDevice>
    fun connect(bluetoothDevice: BluetoothDevice, onConnect: () -> Unit)
    fun send(message: String, onSuccess: () -> Unit)
    fun close(onSuccess: () -> Unit)
}