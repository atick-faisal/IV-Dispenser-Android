package dev.atick.compose.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.bluetooth.models.BluetoothDevice
import dev.atick.bluetooth.repository.BluetoothRepository
import dev.atick.core.utils.Event
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
}