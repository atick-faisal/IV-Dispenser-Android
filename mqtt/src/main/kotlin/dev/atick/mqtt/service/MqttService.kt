package dev.atick.mqtt.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.service.BaseService
import dev.atick.core.utils.Event
import dev.atick.core.utils.extensions.debugMessage
import dev.atick.data.database.room.DispenserDao
import dev.atick.data.models.Dispenser
import dev.atick.data.models.DispenserState
import dev.atick.mqtt.R
import dev.atick.mqtt.repository.MqttRepository
import dev.atick.mqtt.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MqttService : BaseService(), MqttRepository {

    companion object {
        const val MQTT_NOTIFICATION_CHANNEL_ID = "dev.atick.mqtt"
    }

    @Inject
    lateinit var dispenserDao: DispenserDao

    private val persistentNotificationBuilder = NotificationCompat.Builder(
        this, MQTT_NOTIFICATION_CHANNEL_ID
    )

    private var serviceStarted = false
    private var notificationIntent: Intent? = null
    private lateinit var client: Mqtt3AsyncClient
    private val _isClientConnected = MutableLiveData<Event<Boolean>>()
    val isClientConnected: LiveData<Event<Boolean>>
        get() = _isClientConnected

    inner class LocalBinder : Binder() {
        fun getService(): MqttService = this@MqttService
    }

    private val binder = LocalBinder()

    override fun onCreateService() {
        super.onCreateService()
        client = setUpDefaultMqtt3Client(
            onConnected = {
                Logger.i("MQTT CLIENT CONNECTED!")
                _isClientConnected.postValue(Event(true))
                if (serviceStarted) {
                    val notification = persistentNotificationBuilder
                        .setSmallIcon(R.drawable.ic_connected)
                        .setContentTitle(
                            getString(
                                R.string.persistent_notification_title
                            )
                        )
                        .setContentText(
                            getString(
                                R.string.persistent_notification_description
                            )
                        )
                        .build()
                    with(NotificationManagerCompat.from(this)) {
                        notify(PERSISTENT_NOTIFICATION_ID, notification)
                    }
                }
            },
            onDisconnected = {
                Logger.i("MQTT CLIENT DISCONNECTED")
                _isClientConnected.postValue(Event(false))
                val notification = persistentNotificationBuilder
                    .setSmallIcon(R.drawable.ic_warning)
                    .setContentTitle(
                        getString(
                            R.string.persistent_notification_warning_title
                        )
                    )
                    .setContentText(
                        getString(
                            R.string.persistent_notification_warning_description
                        )
                    )
                    .build()
                with(NotificationManagerCompat.from(this)) {
                    notify(PERSISTENT_NOTIFICATION_ID, notification)
                }
            }
        )
    }

    override fun onStartService() {
        Logger.i("STARTING MQTT SERVICE")
        connect(null) {
            subscribe(
                topic = "dev.atick.mqtt/#",
                onSubscribe = {},
                onMessage = {}
            )
        }
        serviceStarted = true
    }

    override fun setupNotification(): Notification {
        try {
            notificationIntent = Intent(
                this, Class.forName("dev.atick.compose.MainActivity")
            )
        } catch (e: ClassNotFoundException) {
            Logger.i("MAIN ACTIVITY NOT FOUND!")
            e.printStackTrace()
        }
        val notification = persistentNotificationBuilder
            .setSmallIcon(R.drawable.ic_warning)
            .setContentTitle(getString(R.string.persistent_notification_warning_title))
            .setContentText(getString(R.string.persistent_notification_warning_description))
            .setPriority(NotificationCompat.PRIORITY_LOW)

        notificationIntent?.let {
            notification.apply {
                val pendingIntent = PendingIntent.getActivity(
                    this@MqttService,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                setContentIntent(pendingIntent)
            }
        }

        return notification.build()
    }

    override fun collectGarbage() {
        this.disconnect {
            debugMessage("Disconnected")
        }
        serviceStarted = false
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun connect(broker: String?, onConnect: () -> Unit) {
        if (_isClientConnected.value?.peekContent() != true) {
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
                Logger.i("MESSAGE SENT SUCCESSFULLY")
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
            onMessage = {
                saveToDatabase(it)
                onMessage.invoke(it)
            },
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

    private fun saveToDatabase(item: String?) {
        item?.let {
            try {
                val dispenser = Json.decodeFromString(Dispenser.serializer(), item)
                val dispenserState = Json.decodeFromString(DispenserState.serializer(), item)
                CoroutineScope(Dispatchers.IO).launch {
                    dispenserDao.insert(dispenser)
                    dispenserDao.insert(dispenserState)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}