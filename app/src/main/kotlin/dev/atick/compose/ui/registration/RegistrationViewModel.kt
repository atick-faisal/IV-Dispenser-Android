package dev.atick.compose.ui.registration

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.bluetooth.repository.BluetoothRepository
import dev.atick.bluetooth.utils.BtUtils
import dev.atick.core.ui.BaseViewModel
import dev.atick.data.models.Response
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val btUtils: BtUtils,
    private val bluetoothRepository: BluetoothRepository
) : BaseViewModel() {
    val incomingMessage = bluetoothRepository.incomingMessage

    private val _pairedDevices = MutableLiveData<List<BluetoothDevice>>()
    val pairedDevices: LiveData<List<BluetoothDevice>>
        get() = _pairedDevices

    private val _connectedDeviceId = MutableLiveData<String?>()
    val connectedDeviceId: LiveData<String?>
        get() = _connectedDeviceId

    private val _isRegistrationComplete = MutableLiveData<Boolean>()
    val isRegistrationComplete: LiveData<Boolean>
        get() = _isRegistrationComplete

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

    fun handleIncomingMessage(message: String) {
        try {
            val response = Json.decodeFromString(
                Response.serializer(),
                message
            )
            _isRegistrationComplete.postValue(response.success)
        } catch (e: SerializationException) {
            Logger.i("PARSING FAILED")
        }
    }

    private fun onConnect(deviceId: String) {
        _connectedDeviceId.postValue(deviceId)
        loader.postValue(null)
    }

    private fun onDisconnect() {
        _connectedDeviceId.postValue(null)
        _isRegistrationComplete.postValue(false)
    }
}