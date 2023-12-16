package dev.atick.compose.utils

import java.text.SimpleDateFormat
import java.util.*

const val N = 10_000_000L

fun getFloatTimestamp(timestamp: Long): Float {
    val shortTimestamp = timestamp / 1000L
    return (shortTimestamp % N).toFloat()
}

fun getFormattedDateTime(timestamp: Long): String {
    val formatter = SimpleDateFormat("h:mm:ss a", Locale.ENGLISH)
    return formatter.format(Date(timestamp))
}

fun getFormattedDateTime(floatTimeStamp: Float): String {
    val shortTimestamp = System.currentTimeMillis() / 1000L
    val div = shortTimestamp / N
    val timestamp = ((N * div) + floatTimeStamp.toLong()) * 1000L
    val formatter = SimpleDateFormat("h:mm:ss a", Locale.ENGLISH)
    return formatter.format(Date((timestamp)))
}