package dev.atick.mqtt.repository

interface MqttRepository {
    fun connect(username: String?, password: String?, onConnect: () -> Unit)
    fun disconnect(onDisconnect: () -> Unit)
    fun publish(topic: String, message: String)
    fun subscribe(topic: String, onSubscribe: () -> Unit, onMessage: (String?) -> Unit)
    fun unsubscribe(topic: String, onUnsubscribe: () -> Unit)
}