package dev.atick.compose.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.atick.compose.database.room.models.Dispenser
import dev.atick.compose.database.room.models.DispenserState

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        Dispenser::class,
        DispenserState::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract val dispenserDao: DispenserDao
    abstract val dispenserStateDao: DispenserStateDao
}