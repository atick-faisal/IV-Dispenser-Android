package dev.atick.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.atick.data.database.room.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val ROOM_DATABASE_NAME = "dev.atick.dispenser.database"

    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext appContext: Context
    ) = Room.databaseBuilder(appContext, AppDatabase::class.java, ROOM_DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()

}