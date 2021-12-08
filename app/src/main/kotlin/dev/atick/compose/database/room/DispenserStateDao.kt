package dev.atick.compose.database.room

import androidx.room.*
import dev.atick.compose.database.room.models.DispenserState
import kotlinx.coroutines.flow.Flow

@Dao
interface DispenserStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dispenserState: DispenserState)

    @Update
    suspend fun update(dispenserState: DispenserState)

    @Delete
    suspend fun delete(dispenserState: DispenserState)

    @Query("SELECT * FROM dispenser_state_table WHERE id = :id")
    suspend fun getItemById(id: Long): DispenserState?

    @Query("SELECT * FROM dispenser_state_table WHERE device_id = :deviceId")
    fun getItemByDeviceId(deviceId: String): Flow<List<DispenserState>>

    @Query("SELECT * FROM dispenser_state_table")
    fun getAllItems(): Flow<List<DispenserState>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<DispenserState>)

    @Query("DELETE FROM dispenser_state_table")
    suspend fun clear()
}