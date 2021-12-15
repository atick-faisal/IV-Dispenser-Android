package dev.atick.bluetooth.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dev.atick.bluetooth.repository.BluetoothRepository
import dev.atick.bluetooth.repository.BluetoothRepositoryImpl

@Module
@InstallIn(ActivityComponent::class)
abstract class BluetoothRepositoryModule {
    @Binds
    abstract fun bindBluetoothRepository(
        bluetoothRepositoryImpl: BluetoothRepositoryImpl
    ): BluetoothRepository
}