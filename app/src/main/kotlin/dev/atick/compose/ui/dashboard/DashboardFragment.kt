package dev.atick.compose.ui.dashboard

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.compose.ui.theme.DispenserTheme
import dev.atick.core.ui.BaseComposeFragment
import dev.atick.core.utils.extensions.debugMessage
import dev.atick.core.utils.extensions.observeEvent
import dev.atick.data.models.Command
import dev.atick.mqtt.repository.MqttRepository
import dev.atick.mqtt.service.MqttService
import kotlinx.serialization.json.Json

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@AndroidEntryPoint
class DashboardFragment : BaseComposeFragment() {

    private val dashboardFragmentArgs: DashboardFragmentArgs by navArgs()
    private val viewModel: DashboardViewModel by viewModels()
    private var deviceId: String? = null

    private lateinit var mqttService: MqttService
    private lateinit var mqttRepository: MqttRepository
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MqttService.LocalBinder
            mqttService = binder.getService()
            mqttRepository = mqttService
            mBound = true

//            mqttRepository.connect(null) {
//                mqttRepository.subscribe(
//                    topic = "dev.atick.mqtt/#",
//                    onSubscribe = {},
//                    onMessage = {
//                        it?.let {
//
//                        }
//                    }
//                )
//            }

            this@DashboardFragment.observeEvent(mqttService.isClientConnected) {
                context?.debugMessage("Client Connected [$it]")
            }

            this@DashboardFragment.observeEvent(mqttService.publishingContent) {
                if (it) viewModel.sendingCommand() else viewModel.commandSent()
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        bindMqttService()
    }

    override fun onStop() {
        super.onStop()
        activity?.unbindService(connection)
        mBound = false
    }

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deviceId = dashboardFragmentArgs.deviceId
        requireContext().debugMessage(deviceId.toString())
        deviceId?.let { id ->
            viewModel.fetchDispenserStates(id)
            viewModel.dispenserStates.observe(this) {
                Logger.i(it.toString())
            }
        }
    }

    private fun bindMqttService() {
        Intent(requireContext(), MqttService::class.java).also { intent ->
            activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun sendCommand(flowRate: Float) {
        if (mBound && mqttService.isClientConnected.value?.peekContent() == true) {
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