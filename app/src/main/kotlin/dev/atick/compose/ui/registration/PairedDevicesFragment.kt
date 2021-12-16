package dev.atick.compose.ui.registration

import android.bluetooth.BluetoothDevice
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.bluetooth.repository.BluetoothRepository
import dev.atick.compose.ui.theme.DispenserTheme
import dev.atick.core.ui.BaseComposeFragment
import dev.atick.core.utils.extensions.debugMessage
import javax.inject.Inject

@ExperimentalAnimationApi
@AndroidEntryPoint
class PairedDevicesFragment : BaseComposeFragment() {

    @Inject
    lateinit var bluetoothRepository: BluetoothRepository
    private val viewModel: RegistrationViewModel by viewModels()

    @Composable
    override fun ComposeUi() {
        DispenserTheme {
            PairedDevicesScreen(::connectToBTDevice)
        }
    }

    override fun observeStates() {
        super.observeStates()
        viewModel.pairedDevices.observe(this) {
            Logger.i(it.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        if (bluetoothRepository.isBluetoothAvailable()) {
            requireActivity().debugMessage("Bluetooth is on")
            viewModel.fetchPairedDevices()
        } else {
            bluetoothRepository.enableBluetooth(requireActivity()) {
                Logger.i("BLUETOOTH ENABLED")
                viewModel.fetchPairedDevices()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        cleanup()
    }

    private fun connectToBTDevice(device: BluetoothDevice) {
        if (device.address == viewModel.connectedDeviceId.value) {
            cleanup()
        } else {
            requireContext().debugMessage("Connecting")
            bluetoothRepository.connect(device) {
                viewModel.onConnect(device.address)
                bluetoothRepository.send("Hello!") {}
            }
        }
    }

    private fun cleanup() {
        bluetoothRepository.close {
            Logger.i("BLUETOOTH SOCKET CLOSED")
            viewModel.onDisconnect()
        }
    }
}