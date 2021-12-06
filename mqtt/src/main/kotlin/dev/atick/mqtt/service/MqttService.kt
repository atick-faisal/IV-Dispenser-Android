package dev.atick.mqtt.service

import android.app.Notification
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.service.BaseService
import dev.atick.mqtt.R
import dev.atick.mqtt.repository.MqttRepository
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
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun connect(broker: String?, onConnect: () -> Unit?) {
        client.connect()
            .thenAccept {
                onConnect.invoke()
            }
    }

    override fun disconnect(onDisconnect: () -> Unit?) {
        TODO("Not yet implemented")
    }

    override fun subscribe(topic: String, onSubscribe: () -> Unit?) {
        TODO("Not yet implemented")
    }

    override fun unsubscribe(topic: String, onUnsubscribe: () -> Unit?) {
        TODO("Not yet implemented")
    }

    override fun listen(topic: String, onMessage: (message: String) -> Unit) {
        TODO("Not yet implemented")
    }
}