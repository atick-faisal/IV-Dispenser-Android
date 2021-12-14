package dev.atick.core.utils.extensions

import android.icu.text.DecimalFormat

fun Float.round(decimals: Int = 2) = "%.00${decimals}f".format(this).toFloat()

fun Float.decimalFormat(): Float {
    val df = DecimalFormat("#.##")
    return df.format(this).toFloat()
}