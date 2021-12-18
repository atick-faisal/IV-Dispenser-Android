package dev.atick.mqtt.utils

import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAckReturnCode
import com.orhanobut.logger.Logger

inline fun Mqtt3AsyncClient.simpleConnect(
    username: String? = null,
    password: String? = null,
    crossinline onSuccess: (Boolean) -> Unit,
    noinline onFailure: ((Mqtt3ConnAckReturnCode) -> Unit)?
) {
    this.connectWith()
        .cleanSession(false)
        .keepAlive(60)
        .simpleAuth()
        .username(username ?: "")
        .password((password ?: "").encodeToByteArray())
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
                    onFailure?.invoke(connAck.returnCode)
                }
            }
        }
}

inline fun Mqtt3AsyncClient.publishOnce(
    topic: String,
    message: String,
    crossinline onSuccess: () -> Unit,
    noinline onFailure: ((Throwable) -> Unit)?
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
                onFailure?.invoke(throwable)
            } else {
                Logger.i("SUCCESSFULLY PUBLISHED! [${message}]")
                onSuccess.invoke()
            }
        }
}

inline fun Mqtt3AsyncClient.simpleSubscribe(
    topic: String,
    crossinline onMessage: (String?) -> Unit,
    crossinline onSuccess: () -> Unit,
    noinline onFailure: ((Throwable) -> Unit)?
) {
    this.subscribeWith()
        .topicFilter(topic)
        .callback { message ->
            Logger.i("[${message.getTopicName()}] -> ${message.getContent()}")
            onMessage.invoke(message.getContent())
        }
        .send()
        .whenComplete { _, throwable ->
            if (throwable != null) {
                Logger.i("SUBSCRIPTION FAILED!")
                onFailure?.invoke(throwable)
            } else {
                Logger.i("SUBSCRIBED SUCCESSFULLY!")
                onSuccess.invoke()
            }
        }
}

inline fun Mqtt3AsyncClient.simpleUnsubscribe(
    topic: String,
    crossinline onSuccess: () -> Unit,
    noinline onFailure: ((Throwable) -> Unit)?
) {
    this.unsubscribeWith()
        .topicFilter(topic)
        .send()
        .whenComplete { _, throwable ->
            if (throwable != null) {
                Logger.i("OPERATION FAILED!")
                onFailure?.invoke(throwable)
            } else {
                Logger.i("UNSUBSCRIBED SUCCESSFULLY!")
                onSuccess.invoke()
            }
        }
}

inline fun Mqtt3AsyncClient.simpleDisconnect(
    crossinline onSuccess: () -> Unit,
    noinline onFailure: ((Throwable) -> Unit)?
) {
    this.disconnect()
        .whenComplete { _, throwable ->
            if (throwable != null) {
                Logger.i("OPERATION FAILED!")
                onFailure?.invoke(throwable)
            } else {
                Logger.i("DISCONNECTED SUCCESSFULLY!")
                onSuccess.invoke()
            }
        }
}