package dev.atick.compose

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.utils.NetworkUtils
import dev.atick.core.utils.extensions.debugMessage
import dev.atick.mqtt.repository.MqttRepository
import dev.atick.mqtt.service.MqttService
import java.util.logging.Logger

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mqttService: MqttService
    private lateinit var mqttRepository: MqttRepository
    private var mBound: Boolean = false
    private lateinit var networkUtils: NetworkUtils

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

            mqttService.isClientConnected.observe(this@MainActivity, {
                it?.let {
                    debugMessage("Client Connected [$it]")
                }
            })
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        networkUtils = NetworkUtils(this)
        networkUtils.isInternetAvailable.observe(this@MainActivity, {
            it?.let {
                // debugMessage("INTERNET AVAILABLE [$it]")
            }
        })
        networkUtils.isWiFiAvailable.observe(this@MainActivity, {
            it?.let {
                // debugMessage("WIFI AVAILABLE [$it]")
            }
        })
        startMqttService()
    }

    override fun onStart() {
        super.onStart()
        Intent(this, MqttService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false
    }

    private fun startMqttService() {
        val intent = Intent(this@MainActivity, MqttService::class.java)
        startService(intent)
    }
}