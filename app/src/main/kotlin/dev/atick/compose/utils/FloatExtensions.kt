package dev.atick.compose.utils

fun Float.round(decimals: Int = 1) = "%.${decimals}f".format(this).toFloat()