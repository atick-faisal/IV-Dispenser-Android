package dev.atick.mqtt.utils

import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAckReturnCode
import com.orhanobut.logger.Logger

inline fun Mqtt3AsyncClient.simpleConnect(
    crossinline onSuccess: (Boolean) -> Unit,
    crossinline onFailure: (Mqtt3ConnAckReturnCode) -> Unit = {}
) {
    this.connectWith()
        .cleanSession(false)
        .keepAlive(60)
        .simpleAuth()
        .username("admin")
        .password("admin".encodeToByteArray())
        .applySimpleAuth()
        .send()
        .thenAccept { connAck ->
            when (connAck.returnCode) {
                Mqtt3ConnAckReturnCode.SUCCESS -> {
                    Logger.i("CONNECTION SUCCESSFUL! [${connAck.returnCode}]")
                    onSuccess(connAck.isSessionPresent)
                }
                else -> {
                    Logger.i("CONNECTION FAILED! [${connAck.returnCode}]")
                    onFailure(connAck.returnCode)
                }
            }
        }
}

inline fun Mqtt3AsyncClient.publishOnce(
    topic: String,
    message: String,
    crossinline onSuccess: () -> Unit = {},
    crossinline onFailure: () -> Unit = {}
) {
    this.publishWith()
        .topic(topic)
        .payload(message.encodeToByteArray())
        .qos(MqttQos.AT_LEAST_ONCE)
        .retain(true)
        .send()
        .whenComplete { _, throwable ->
            if (throwable != null) {
                Logger.i("PUBLISH FAILED! [${message}]")
                throwable.printStackTrace()
                onFailure.invoke()
            } else {
                Logger.i("SUCCESSFULLY PUBLISHED! [${message}]")
                onSuccess.invoke()
            }
        }
}

inline fun Mqtt3AsyncClient.simpleSubscribe(
    topic: String,
    crossinline onMessage: () -> Unit = {},
    crossinline onSuccess: () -> Unit = {},
    crossinline onFailure: () -> Unit = {}
) {
    this.subscribeWith()
        .topicFilter(topic)
        .callback { message ->
            Logger.i("[${message.getTopicName()}] -> ${message.getContent()}")
        }
        .send()
        .whenComplete { _, throwable ->
            if (throwable != null) {
                Logger.i("SUBSCRIPTION FAILED!")
                throwable.printStackTrace()
                onFailure.invoke()
            } else {
                Logger.i("SUBSCRIBED SUCCESSFULLY!")
                onSuccess.invoke()
            }
        }
}