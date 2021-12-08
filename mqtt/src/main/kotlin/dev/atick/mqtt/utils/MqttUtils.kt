package dev.atick.mqtt.utils

import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish
import dev.atick.mqtt.di.MqttModule
import java.nio.charset.StandardCharsets
import java.util.*

fun Mqtt3Publish.getTopicName(): String {
    val byteBuffer = this.topic.toByteBuffer()
    return StandardCharsets.UTF_8.decode(byteBuffer).toString()
}

fun Mqtt3Publish.getContent(): String? {
    val byteArray = this.payloadAsBytes
    return if (byteArray.isEmpty()) {
        null
    } else {
        byteArray.toString(Charsets.UTF_8)
    }
}

inline fun setUpDefaultMqtt3Client(
    crossinline onConnected: () -> Unit,
    crossinline onDisconnected: () -> Unit
): Mqtt3AsyncClient {
    return Mqtt3Client.builder()
        .identifier(UUID.randomUUID().toString())
        .serverHost(MqttModule.BROKER_URL)
        .serverPort(1883)
        .automaticReconnectWithDefaultConfig()
        .addConnectedListener { onConnected.invoke() }
        .addDisconnectedListener { onDisconnected.invoke() }
        .buildAsync()
}