package dev.atick.core.utils.extensions

fun Float.round(decimals: Int = 2) = "%.${decimals}f".format(this).toFloat()