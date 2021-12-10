package dev.atick.compose.utils

import java.text.SimpleDateFormat
import java.util.*

fun getFormattedDateTime(timestamp: Long): String {
    val formatter = SimpleDateFormat("h:mm a", Locale.ENGLISH)
    return formatter.format(Date(timestamp))
}

fun getFormattedDateTime(timestamp: Float): String {
    val formatter = SimpleDateFormat("h:mm a", Locale.ENGLISH)
    return formatter.format(Date(timestamp.toLong()))
}