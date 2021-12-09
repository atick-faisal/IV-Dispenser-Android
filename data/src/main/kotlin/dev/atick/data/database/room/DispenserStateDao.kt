package dev.atick.data.database.room

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.atick.data.models.DispenserState

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
    fun getItemByDeviceId(deviceId: String): LiveData<List<DispenserState>>

    @Query("SELECT * FROM dispenser_state_table")
    fun getAllItems(): LiveData<List<DispenserState>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<DispenserState>)

    @Query("DELETE FROM dispenser_state_table")
    suspend fun clear()
}