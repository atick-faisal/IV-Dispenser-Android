package dev.atick.compose.ui.registration

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.bluetooth.repository.BluetoothRepository
import dev.atick.core.ui.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val bluetoothRepository: BluetoothRepository
) : BaseViewModel() {
    val incomingMessage = bluetoothRepository.incomingMessage

    private val _pairedDevices = MutableLiveData<List<BluetoothDevice>>()
    val pairedDevices: LiveData<List<BluetoothDevice>>
        get() = _pairedDevices

    private val _connectedDeviceId = MutableLiveData<String?>()
    val connectedDeviceId: LiveData<String?>
        get() = _connectedDeviceId

    fun fetchPairedDevices() {
        viewModelScope.launch {
            _pairedDevices.value = bluetoothRepository.getPairedDevicesList()
        }
    }

    fun connectToBTDevice(device: BluetoothDevice) {
        if (device.address == connectedDeviceId.value) {
            closeBluetoothConnection()
        } else {
            loader.postValue("Connecting to the Device ... ")
            bluetoothRepository.connect(device) {
                onConnect(device.address)
            }
        }
    }

    fun registerDevice(registrationInfo: String) {
        loader.postValue("Registering Device ... ")
        bluetoothRepository.send(registrationInfo) {
            Logger.i("SENDING REGISTRATION INFO")
        }
    }

    fun closeBluetoothConnection() {
        bluetoothRepository.close {
            Logger.i("BLUETOOTH SOCKET CLOSED")
            onDisconnect()
        }
    }

    private fun onConnect(deviceId: String) {
        _connectedDeviceId.postValue(deviceId)
        loader.postValue(null)
    }

    private fun onDisconnect() {
        _connectedDeviceId.postValue(null)
    }
}