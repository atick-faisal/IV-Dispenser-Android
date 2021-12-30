package dev.atick.mqtt.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.mqtt.repository.MqttRepository
import dev.atick.mqtt.repository.MqttRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMqttRepository(
        mqttRepositoryImpl: MqttRepositoryImpl
    ): MqttRepository
}