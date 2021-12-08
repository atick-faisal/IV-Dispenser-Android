package dev.atick.compose.ui.home

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.compose.ui.theme.DispenserTheme
import dev.atick.core.ui.BaseComposeFragment
import dev.atick.core.utils.extensions.debugMessage
import dev.atick.mqtt.repository.MqttRepository
import dev.atick.mqtt.service.MqttService

@AndroidEntryPoint
class HomeFragment : BaseComposeFragment() {

    private lateinit var mqttService: MqttService
    private lateinit var mqttRepository: MqttRepository
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MqttService.LocalBinder
            mqttService = binder.getService()
            mqttRepository = mqttService
            mBound = true

            mqttRepository.connect(null) {
                mqttRepository.subscribe(
                    topic = "dev.atick.mqtt/#",
                    onSubscribe = {},
                    onMessage = {
                        it?.let {

                        }
                    }
                )
            }

            mqttService.isClientConnected.observe(this@HomeFragment, {
                it?.let {
                    context?.debugMessage("Client Connected [$it]")
                }
            })
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(requireContext(), MqttService::class.java).also { intent ->
            activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        activity?.unbindService(connection)
        mBound = false
    }

    @Composable
    override fun ComposeUi() {
        DispenserTheme {
            HomeScreen()
        }
    }
}