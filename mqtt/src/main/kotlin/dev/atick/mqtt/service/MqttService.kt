package dev.atick.mqtt.service

import android.app.Notification
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.service.BaseService
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

    @Inject
    lateinit var dispenserDao: DispenserDao

    private lateinit var client: Mqtt3AsyncClient
    private val _isClientConnected = MutableLiveData<Boolean>()
    val isClientConnected: LiveData<Boolean>
        get() = _isClientConnected

    companion object {
        const val MQTT_NOTIFICATION_CHANNEL_ID = "dev.atick.mqtt"
    }

    inner class LocalBinder : Binder() {
        fun getService(): MqttService = this@MqttService
    }

    private val binder = LocalBinder()

    override fun onCreateService() {
        super.onCreateService()
        client = setUpDefaultMqtt3Client(
            onConnected = {
                Logger.i("MQTT CLIENT CONNECTED!")
                _isClientConnected.postValue(true)
            },
            onDisconnected = {
                Logger.i("MQTT CLIENT DISCONNECTED")
                _isClientConnected.postValue(false)
            }
        )
    }

    override fun onStartService() {
        Logger.i("STARTING MQTT SERVICE")
        _isClientConnected.value?.let { isClientConnected ->
            if (!isClientConnected) {
                connect(null) {}
            }
        }
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
                val dispenser = Json.decodeFromString(Dispenser.serializer() ,item)
                val dispenserState = Json.decodeFromString(DispenserState.serializer() ,item)
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