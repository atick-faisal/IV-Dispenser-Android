package dev.atick.bluetooth.repository

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import dev.atick.bluetooth.models.BluetoothDevice
import javax.inject.Inject

class BluetoothRepositoryImpl @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter?
) : BluetoothRepository {

    override fun isBluetoothAvailable(): Boolean {
        return bluetoothAdapter?.isEnabled ?: false
    }

    override fun enableBluetooth(activity: ComponentActivity, onActivityResult: () -> Unit) {
        val resultLauncher = activity
            .registerForActivityResult(StartActivityForResult()) { result ->
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        onActivityResult.invoke()
                    }
                }
            }
        bluetoothAdapter?.let {
            if (!it.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                resultLauncher.launch(enableBtIntent)
            }
        }
    }

    override fun getPairedDevicesList(): List<BluetoothDevice> {
        val bondedDevices = bluetoothAdapter?.bondedDevices
        val pairedDevices = mutableListOf<BluetoothDevice>()
        bondedDevices?.forEach { device ->
            pairedDevices.add(
                BluetoothDevice(
                    name = device.name,
                    macAddress = device.address
                )
            )
        }
        return pairedDevices.toList()
    }
}