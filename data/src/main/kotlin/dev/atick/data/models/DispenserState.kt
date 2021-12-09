package dev.atick.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Entity(
    tableName = "dispenser_state_table"
)
@Serializable
data class DispenserState(
    @PrimaryKey(autoGenerate = true)
    @Transient
    val id: Long = 0,

    @ColumnInfo(name = "timestamp")
    @SerialName("timestamp")
    val timestamp: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "device_id")
    @SerialName("device_id")
    val deviceId: String,

    @ColumnInfo(name = "room")
    @SerialName("room")
    val room: String?,

    @ColumnInfo(name = "drip_rate")
    @SerialName("drip_rate")
    val dripRate: Float?,

    @ColumnInfo(name = "flow_rate")
    @SerialName("flow_rate")
    val flowRate: Float,

    @ColumnInfo(name = "urine_out")
    @SerialName("urine_out")
    val urineOut: Float
)
