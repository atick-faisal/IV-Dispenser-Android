package dev.atick.mqtt.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.orhanobut.logger.Logger
import dev.atick.core.utils.Event
import dev.atick.data.database.room.DispenserDao
import dev.atick.data.models.Dispenser
import dev.atick.data.models.DispenserState
import dev.atick.mqtt.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MqttRepositoryImpl @Inject constructor(
    private val dispenserDao: DispenserDao
) : MqttRepository {

    private lateinit var client: Mqtt3AsyncClient

    private val _isClientConnected = MutableLiveData<Event<Boolean>>()
    override val isClientConnected: LiveData<Event<Boolean>>
        get() = _isClientConnected

    override fun initializeClient(onConnected: () -> Unit, onDisconnected: () -> Unit) {
        client = setUpDefaultMqtt3Client(
            onConnected = {
                Logger.i("MQTT CLIENT CONNECTED!")
                _isClientConnected.postValue(Event(true))
                onConnected.invoke()
            },
            onDisconnected = {
                Logger.i("MQTT CLIENT DISCONNECTED")
                _isClientConnected.postValue(Event(false))
                onDisconnected.invoke()
            }
        )
    }

    override fun connect(username: String?, password: String?, onConnect: () -> Unit) {
        if (_isClientConnected.value?.peekContent() != true) {
            Logger.i("CONNECTING ... ")
            client.simpleConnect(
                username = username,
                password = password,
                onSuccess = { returnCode ->
                    Logger.i("RETURN CODE: [$returnCode]")
                    onConnect.invoke()
                },
                onFailure = {
                    Logger.e("CONNECTION FAILED!")
                }
            )
        }
    }

    override fun disconnect(onDisconnect: () -> Unit) {
        client.simpleDisconnect(
            onSuccess = onDisconnect,
            onFailure = {
                Logger.e("CONNECTION FAILED!")
                it.printStackTrace()
            }
        )
    }

    override fun publish(topic: String, message: String, onSuccess: () -> Unit) {
        Logger.i("PUBLISHING ... ")
        client.publishOnce(
            topic = topic,
            message = message,
            onSuccess = {
                Logger.i("MESSAGE SENT SUCCESSFULLY")
                onSuccess.invoke()
            },
            onFailure = {
                Logger.e("FAILED TO PUBLISH!")
                it.printStackTrace()
            }
        )
    }

    override fun subscribe(
        topic: String,
        onSubscribe: () -> Unit,
        onMessage: (String?) -> Unit,
        onAlert: (String) -> Unit
    ) {
        Logger.i("SUBSCRIBING ... ")
        client.simpleSubscribe(
            topic = topic,
            onMessage = {
                saveToDatabase(it, onAlert)
                onMessage.invoke(it)
            },
            onSuccess = onSubscribe,
            onFailure = {
                Logger.e("FAILED TO SUBSCRIBE!")
                it.printStackTrace()
            }
        )
    }

    override fun unsubscribe(topic: String, onUnsubscribe: () -> Unit) {
        client.simpleUnsubscribe(
            topic = topic,
            onSuccess = onUnsubscribe,
            onFailure = {
                Logger.e("FAILED TO UNSUBSCRIBE!")
                it.printStackTrace()
            }
        )
    }

    private fun saveToDatabase(item: String?, onAlert: (String) -> Unit) {
        item?.let {
            try {
                val dispenser = Json.decodeFromString(Dispenser.serializer(), item)
                val dispenserState = Json.decodeFromString(DispenserState.serializer(), item)
                if (!dispenserState.alertMessage.isNullOrEmpty()) {
                    dispenser.alertMessage?.let { onAlert(it) }
                }
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