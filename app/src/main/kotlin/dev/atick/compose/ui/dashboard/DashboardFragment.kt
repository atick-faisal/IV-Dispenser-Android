package dev.atick.compose.ui.dashboard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.compose.ui.theme.DispenserTheme
import dev.atick.core.ui.BaseComposeFragment
import dev.atick.core.utils.extensions.debugMessage
import dev.atick.core.utils.extensions.observeEvent
import dev.atick.mqtt.repository.MqttRepository
import javax.inject.Inject

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@AndroidEntryPoint
class DashboardFragment : BaseComposeFragment() {

    @Inject
    lateinit var mqttRepository: MqttRepository

    @Composable
    override fun ComposeUi() {
        DispenserTheme {
            DashboardScreen()
        }
    }

    override fun observeStates() {
        super.observeStates()

        observeEvent(mqttRepository.isClientConnected) {
            context?.debugMessage("Client Connected [$it]")
        }
    }
}