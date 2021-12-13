package dev.atick.data.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.atick.data.models.Dispenser
import dev.atick.data.models.DispenserState

@Database(
    version = 5,
    exportSchema = false,
    entities = [
        Dispenser::class,
        DispenserState::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dispenserDao(): DispenserDao
}