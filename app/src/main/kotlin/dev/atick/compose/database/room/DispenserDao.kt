package dev.atick.compose.database.room

import androidx.room.*
import dev.atick.compose.database.room.models.Dispenser
import kotlinx.coroutines.flow.Flow

@Dao
interface DispenserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dispenser: Dispenser)

    @Update
    suspend fun update(dispenser: Dispenser)

    @Delete
    suspend fun delete(dispenser: Dispenser)

    @Query("SELECT * FROM dispenser_table")
    fun getAllItems(): Flow<List<Dispenser>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Dispenser>)

    @Query("DELETE FROM dispenser_table")
    suspend fun clear()
}