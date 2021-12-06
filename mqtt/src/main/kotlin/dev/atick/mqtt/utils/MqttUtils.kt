package dev.atick.mqtt.utils

import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish
import java.nio.charset.StandardCharsets

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