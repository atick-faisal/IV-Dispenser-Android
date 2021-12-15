package dev.atick.compose.ui.registration

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.bluetooth.repository.BluetoothRepository
import dev.atick.compose.ui.theme.DispenserTheme
import dev.atick.core.ui.BaseComposeFragment
import javax.inject.Inject

@ExperimentalAnimationApi
@AndroidEntryPoint
class PairedDevicesFragment : BaseComposeFragment() {

    @Inject
    lateinit var bluetoothRepository: BluetoothRepository

    @Composable
    override fun ComposeUi() {
        DispenserTheme {
            PairedDevicesScreen()
        }
    }

    override fun onStart() {
        super.onStart()
        bluetoothRepository.enableBluetooth(requireActivity()) {
            Logger.i("BLUETOOTH ENABLED")
        }
    }
}