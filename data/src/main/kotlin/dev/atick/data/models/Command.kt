package dev.atick.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Command(
    @SerialName("device_id")
    val deviceId: String,
    @SerialName("flow_rate")
    val flowRate: Float
)
