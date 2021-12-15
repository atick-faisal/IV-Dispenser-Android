package dev.atick.bluetooth.repository

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity

class BlurtoothRepositoryImpl : BluetoothRepository {
    override fun enableBluetooth(activity: ComponentActivity, onActivityResult: () -> Unit) {
        val bluetoothManager = activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val resultLauncher = activity.registerForActivityResult(StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> { onActivityResult.invoke() }
            }
        }
        bluetoothAdapter?.let {
            if (!it.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                resultLauncher.launch(enableBtIntent)
            }
        }
    }
}