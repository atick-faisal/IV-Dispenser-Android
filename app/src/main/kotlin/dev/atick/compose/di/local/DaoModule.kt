package dev.atick.compose.di.local

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.compose.database.room.AppDatabase
import javax.inject.Singleton

@Module(
    includes = [
        DatabaseModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Singleton
    @Provides
    fun provideItemDao(appDatabase: AppDatabase) = appDatabase.dispenserDao

}