package dev.atick.compose.utils

fun Float.round(decimals: Int = 2) = "%.${decimals}f".format(this).toFloat()