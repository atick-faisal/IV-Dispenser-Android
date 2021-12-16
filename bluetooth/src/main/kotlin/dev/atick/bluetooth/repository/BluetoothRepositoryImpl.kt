package dev.atick.bluetooth.repository

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import javax.inject.Inject

class BluetoothRepositoryImpl @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter?
) : BluetoothRepository {

    companion object {
        private val BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    private var mmSocket: BluetoothSocket? = null
    private val mmBuffer: ByteArray = ByteArray(1024)

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
            pairedDevices.add(device)
        }
        return pairedDevices.toList()
    }

    override fun connect(bluetoothDevice: BluetoothDevice, onConnect: () -> Unit) {
        if (mmSocket == null) {
            mmSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(BT_UUID)
            bluetoothAdapter?.cancelDiscovery()
        } else {
            try {
                mmSocket?.close()
                mmSocket =
                    bluetoothDevice.createInsecureRfcommSocketToServiceRecord(BT_UUID)
            } catch (e: IOException) {}
        }
        CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                mmSocket?.connect()
            }.onSuccess {
                Logger.i("BLUETOOTH DEVICE CONNECTED")
                onConnect.invoke()
            }.onFailure { throwable ->
                Logger.i("CONNECTION FAILED")
                throwable.printStackTrace()
            }
        }
    }

    override fun send(message: String, onSuccess: () -> Unit) {
        mmSocket?.let { socket ->
            try {
                socket.outputStream.write(message.toByteArray())
                onSuccess.invoke()
            } catch (e: IOException) {
                Logger.i("SENDING FAILED")
                e.printStackTrace()
            }
        }
    }

    override fun close(onSuccess: () -> Unit) {
        mmSocket?.let { socket ->
            try {
                socket.close()
                onSuccess.invoke()
            } catch (e: IOException) {
                Logger.i("COULDN'T CLOSE SOCKET")
                e.printStackTrace()
            }
        }
    }
}