package dev.atick.compose.utils

import java.text.SimpleDateFormat
import java.util.*

fun getFormattedDateTime(timestamp: Long): String {
    val formatter = SimpleDateFormat("h:mm:ss a", Locale.ENGLISH)
    return formatter.format(Date(timestamp))
}

fun getFormattedDateTime(timestamp: Float): String {
    val formatter = SimpleDateFormat("mm:ss", Locale.ENGLISH)
    val n = (System.currentTimeMillis() / 1000000L)
    return formatter.format(Date((n * 1000000L + timestamp.toLong())))
}