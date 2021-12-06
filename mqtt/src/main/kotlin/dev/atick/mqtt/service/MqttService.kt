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
import dev.atick.mqtt.R
import dev.atick.mqtt.repository.MqttRepository
import dev.atick.mqtt.utils.publishOnce
import dev.atick.mqtt.utils.simpleConnect
import dev.atick.mqtt.utils.simpleSubscribe
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

    override fun initService() {

    }

    override fun doInBackground() {
        Logger.i("DO IN BACKGROUND")
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
        client.disconnect()
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun connect(broker: String?, onConnect: () -> Unit?) {
        Logger.i("CONNECTING ... ")
        client.simpleConnect(
            onSuccess = { returnCode ->
                Logger.i("RETURN CODE: [$returnCode]")
                onConnect.invoke()
            }
        )
    }

    override fun disconnect(onDisconnect: () -> Unit?) {
        TODO("Not yet implemented")
    }

    override fun publish(topic: String, message: String) {
        client.publishOnce(topic, message)
    }

    override fun subscribe(topic: String, onSubscribe: (() -> Unit)?) {
        Logger.i("SUBSCRIBING ... ")
        client.simpleSubscribe("dev.atick.mqtt")
    }

    override fun unsubscribe(topic: String, onUnsubscribe: () -> Unit?) {
        TODO("Not yet implemented")
    }

    override fun listen(topic: String, onMessage: (message: String) -> Unit) {
        TODO("Not yet implemented")
    }
}