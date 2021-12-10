package dev.atick.mqtt.di

import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.mqtt.repository.MqttRepository
import dev.atick.mqtt.service.MqttService
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MqttModule {

    const val BROKER_URL = "broker.emqx.io"

    @Singleton
    @Provides
    fun provideDefaultMqtt3Client(): Mqtt3AsyncClient {
        return Mqtt3Client.builder()
            .identifier(UUID.randomUUID().toString())
            .serverHost(BROKER_URL)
            .serverPort(1883)
            .automaticReconnectWithDefaultConfig()
            .buildAsync()
    }

    @Singleton
    @Provides
    fun provideDefaultMqtt5Client(): Mqtt5AsyncClient {
        return Mqtt5Client.builder()
            .identifier(UUID.randomUUID().toString())
            .serverHost(BROKER_URL)
            .serverPort(1883)
            .automaticReconnectWithDefaultConfig()
            .buildAsync()
    }

}