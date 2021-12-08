package dev.atick.compose.database.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(
    tableName = "dispenser_table"
)
@Serializable
data class Dispenser(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "device_id")
    @SerialName("device_id")
    val deviceId: String,

    @ColumnInfo(name = "timestamp")
    @SerialName("timestamp")
    val timestamp: Long = System.currentTimeMillis(),

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
