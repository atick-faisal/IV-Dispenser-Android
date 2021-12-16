package dev.atick.compose.ui.registration

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.bluetooth.repository.BluetoothRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val bluetoothRepository: BluetoothRepository
) : ViewModel() {
    private val _pairedDevices = MutableLiveData<List<BluetoothDevice>>()
    val pairedDevices: LiveData<List<BluetoothDevice>>
        get() = _pairedDevices

    fun fetchPairedDevices() {
        viewModelScope.launch {
            _pairedDevices.value = bluetoothRepository.getPairedDevicesList()
        }
    }

    private val _connectedDeviceId = MutableLiveData<String?>()
    val connectedDeviceId: LiveData<String?>
        get() = _connectedDeviceId

    private val socket: BluetoothSocket? = null

    fun onConnect(deviceId: String) {
        _connectedDeviceId.postValue(deviceId)
    }

    fun onDisconnect() {
        _connectedDeviceId.postValue(null)
    }
}