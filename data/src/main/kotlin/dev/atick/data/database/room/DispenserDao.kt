package dev.atick.data.database.room

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.atick.data.models.Dispenser
import dev.atick.data.models.DispenserState
import dev.atick.data.models.relations.DispenserWithStates

@Dao
interface DispenserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dispenser: Dispenser)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dispenserState: DispenserState)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDispensers(items: List<Dispenser>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStates(items: List<DispenserState>)

    @Update
    suspend fun update(dispenser: Dispenser)

    @Update
    suspend fun update(dispenserState: DispenserState)

    @Delete
    suspend fun delete(dispenserState: DispenserState)

    @Delete
    suspend fun delete(dispenser: Dispenser)

    @Query("SELECT * FROM dispenser_table WHERE device_id = :deviceId LIMIT 1")
    suspend fun getDispenserById(deviceId: String): Dispenser?

    @Query("SELECT * FROM dispenser_state_table WHERE id = :id LIMIT 1")
    suspend fun getDispenserStateById(id: Long): DispenserState?

    @Query("SELECT * FROM dispenser_table ORDER BY room ASC")
    fun getAllDispensers(): LiveData<List<Dispenser>>

    @Query("SELECT * FROM dispenser_state_table ORDER BY id DESC")
    fun getAllStates(): LiveData<List<DispenserState>>

    @Transaction
    @Query("SELECT * FROM dispenser_table WHERE device_id = :deviceId LIMIT 1")
    fun getDispenserWithStatesByDeviceId(deviceId: String): LiveData<DispenserWithStates?>

    @Query("SELECT * FROM dispenser_state_table WHERE device_id = :deviceId ORDER BY id DESC LIMIT :n")
    fun getStatesByDeviceId(deviceId: String, n: Int): LiveData<List<DispenserState>>

    @Query("DELETE FROM dispenser_table")
    suspend fun deleteAllDispensers()

    @Query("DELETE FROM dispenser_state_table")
    suspend fun deleteAllStates()
}