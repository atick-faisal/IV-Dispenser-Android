package dev.atick.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Register(
    @SerialName("room_number")
    val roomNumber: String,

    @SerialName("wifi_name")
    val wifiName: String,

    @SerialName("wifi_password")
    val wifiPassword: String
)