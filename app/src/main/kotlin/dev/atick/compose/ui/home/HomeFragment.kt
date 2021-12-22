package dev.atick.compose.ui.home

import androidx.compose.runtime.Composable
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.compose.ui.theme.DispenserTheme
import dev.atick.core.ui.BaseComposeFragment
import dev.atick.core.utils.extensions.debugMessage
import dev.atick.core.utils.extensions.observeEvent
import dev.atick.mqtt.repository.MqttRepository
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseComposeFragment() {

    @Inject
    lateinit var mqttRepository: MqttRepository

    @Composable
    override fun ComposeUi() {
        DispenserTheme {
            HomeScreen(
                ::navigateToDashboardFragment,
                ::navigateToRegisterFragment
            )
        }
    }

    override fun observeStates() {
        super.observeStates()
        observeEvent(mqttRepository.isClientConnected) {
            context?.debugMessage("Client Connected [$it]")
        }
    }

    private fun navigateToDashboardFragment(deviceId: String) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDashboardFragment(deviceId)
        )
    }

    private fun navigateToRegisterFragment() {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToRegistrationFragment()
        )
    }
}