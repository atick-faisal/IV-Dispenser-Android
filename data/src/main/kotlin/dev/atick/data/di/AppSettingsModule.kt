package dev.atick.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.data.database.datastore.AppSettings
import dev.atick.data.database.datastore.AppSettingsImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class AppSettingsModule {

    @Binds
    abstract fun bindAppSettings(
        appSettingsImpl: AppSettingsImpl
    ): AppSettings
}