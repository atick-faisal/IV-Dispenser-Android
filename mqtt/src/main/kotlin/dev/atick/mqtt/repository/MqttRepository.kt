package dev.atick.mqtt.repository

import androidx.lifecycle.LiveData
import dev.atick.core.utils.Event

interface MqttRepository {
    val isClientConnected: LiveData<Event<Boolean>>

    fun initializeClient(
        onConnected: () -> Unit,
        onDisconnected: () -> Unit
    )

    fun connect(
        username: String?,
        password: String?,
        onConnect: () -> Unit
    )

    fun disconnect(
        onDisconnect: () -> Unit
    )

    fun publish(
        topic: String,
        message: String,
        onSuccess: () -> Unit
    )

    fun subscribe(
        topic: String,
        onSubscribe: () -> Unit,
        onMessage: (String?) -> Unit,
        onAlert: (String) -> Unit
    )

    fun unsubscribe(
        topic: String,
        onUnsubscribe: () -> Unit
    )
}