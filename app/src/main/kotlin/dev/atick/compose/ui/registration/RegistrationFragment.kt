package dev.atick.compose.ui.registration

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.bluetooth.repository.BluetoothRepository
import dev.atick.compose.ui.theme.DispenserTheme
import dev.atick.core.ui.BaseComposeFragment
import dev.atick.core.utils.extensions.debugMessage
import javax.inject.Inject

@ExperimentalAnimationApi
@AndroidEntryPoint
class RegistrationFragment : BaseComposeFragment() {

    @Inject
    lateinit var bluetoothRepository: BluetoothRepository
    private val viewModel: RegistrationViewModel by viewModels()

    @Composable
    override fun ComposeUi() {
        DispenserTheme {
            RegistrationScreen(::navigateToHomeFragment)
        }
    }

    override fun observeStates() {
        super.observeStates()
        viewModel.pairedDevices.observe(this) {
            Logger.i(it.toString())
        }
        viewModel.incomingMessage.observe(this) {
            viewModel.handleIncomingMessage(it)
            requireContext().debugMessage(it)
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
        viewModel.closeBluetoothConnection()
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(
            RegistrationFragmentDirections.actionRegistrationFragmentToHomeFragment()
        )
    }
}