package dev.atick.bluetooth.repository

import androidx.activity.ComponentActivity

interface BluetoothRepository {
    fun enableBluetooth(activity: ComponentActivity, onActivityResult: () -> Unit)
}