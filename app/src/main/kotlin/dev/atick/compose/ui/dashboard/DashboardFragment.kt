package dev.atick.compose.ui.dashboard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.compose.ui.theme.DispenserTheme
import dev.atick.core.ui.BaseComposeFragment
import dev.atick.core.utils.extensions.debugMessage
import dev.atick.core.utils.extensions.observeEvent
import dev.atick.data.models.Command
import dev.atick.mqtt.repository.MqttRepository
import kotlinx.serialization.json.Json
import javax.inject.Inject

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@AndroidEntryPoint
class DashboardFragment : BaseComposeFragment() {

    @Inject
    lateinit var mqttRepository: MqttRepository

    private val viewModel: DashboardViewModel by viewModels()
    private var deviceId: String? = null

    @Composable
    override fun ComposeUi() {
        DispenserTheme {
            DashboardScreen()
        }
    }

    override fun observeStates() {
        super.observeStates()
        observeEvent(viewModel.flowRate) {
            sendCommand(it)
        }
        observeEvent(mqttRepository.isClientConnected) {
            context?.debugMessage("Client Connected [$it]")
        }

        observeEvent(mqttRepository.publishingContent) {
            if (it) viewModel.sendingCommand() else viewModel.commandSent()
        }
    }

    private fun sendCommand(flowRate: Float) {
        if (mqttRepository.isClientConnected.value?.peekContent() == true) {
            deviceId?.let { deviceId ->
                viewModel.sendingCommand()
                mqttRepository.publish(
                    topic = "dev.atick.mqtt/command/${deviceId}",
                    message = Json.encodeToString(
                        Command.serializer(),
                        Command(
                            deviceId = deviceId,
                            flowRate = flowRate
                        )
                    )
                )
            }
        }
    }
}