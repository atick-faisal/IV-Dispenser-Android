package dev.atick.mqtt.service

import android.app.Notification
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.service.BaseService
import dev.atick.core.utils.extensions.debugMessage
import dev.atick.mqtt.R
import dev.atick.mqtt.repository.MqttRepository
import dev.atick.mqtt.utils.*
import javax.inject.Inject

@AndroidEntryPoint
class MqttService : BaseService(), MqttRepository {

    @Inject
    lateinit var client: Mqtt3AsyncClient

    companion object {
        const val MQTT_NOTIFICATION_CHANNEL_ID = "dev.atick.mqtt"
    }

    inner class LocalBinder : Binder() {
        fun getService(): MqttService = this@MqttService
    }

    private val binder = LocalBinder()

    override fun onStartService() {
        Logger.i("STARTING MQTT SERVICE")

    }

    override fun setupNotification(): Notification {
        return NotificationCompat
            .Builder(this, MQTT_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_android)
            .setContentTitle(getString(R.string.persistent_notification_title))
            .setContentText(getString(R.string.persistent_notification_description))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun collectGarbage() {
        this.disconnect {
            debugMessage("Disconnected")
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun connect(broker: String?, onConnect: () -> Unit) {
        Logger.i("CONNECTING ... ")
        client.simpleConnect(
            onSuccess = { returnCode ->
                Logger.i("RETURN CODE: [$returnCode]")
                onConnect.invoke()
            },
            onFailure = {
                debugMessage(it.name)
            }
        )
    }

    override fun disconnect(onDisconnect: () -> Unit) {
        client.simpleDisconnect(
            onSuccess = onDisconnect,
            onFailure = {
                debugMessage("Failed to Disconnect")
                it.printStackTrace()
            }
        )
    }

    override fun publish(topic: String, message: String) {
        Logger.i("PUBLISHING ... ")
        client.publishOnce(
            topic = topic,
            message = message,
            onSuccess = {
                debugMessage("Message Sent Successfully")
            },
            onFailure = {
                debugMessage("Failed to Send Message")
                it.printStackTrace()
            }
        )
    }

    override fun subscribe(topic: String, onSubscribe: () -> Unit, onMessage: (String?) -> Unit) {
        Logger.i("SUBSCRIBING ... ")
        client.simpleSubscribe(
            topic = topic,
            onMessage = onMessage,
            onSuccess = onSubscribe,
            onFailure = {
                debugMessage("Failed to Subscribe")
                it.printStackTrace()
            }
        )
    }

    override fun unsubscribe(topic: String, onUnsubscribe: () -> Unit) {
        client.simpleUnsubscribe(
            topic = topic,
            onSuccess = onUnsubscribe,
            onFailure = {
                debugMessage("Failed to Unsubscribe")
                it.printStackTrace()
            }
        )
    }
}