package dev.atick.mqtt.repository

interface MqttRepository {
    fun connect(broker: String?, onConnect: () -> Unit?)
    fun disconnect(onDisconnect: () -> Unit?)
    fun subscribe(topic: String, onSubscribe: () -> Unit?)
    fun unsubscribe(topic: String, onUnsubscribe: () -> Unit?)
    fun listen(topic: String, onMessage: (message: String) -> Unit)
}