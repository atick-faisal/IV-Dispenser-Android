package dev.atick.compose.ui.theme

import ai.atick.material.MaterialColor
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.atick.compose.R

private val DarkColorPalette = darkColors(
    primary = Primary,
    primaryVariant = PrimaryVariant,
    secondary = MaterialColor.Orange700,
    secondaryVariant = MaterialColor.Orange600,
    background = Color(0xFF_202430),
    surface = Color(0xFF_2E3440),
    error = MaterialColor.Red700,
    onPrimary = MaterialColor.White,
    onSecondary = MaterialColor.White,
    onBackground = MaterialColor.BlueGray50,
    onSurface = MaterialColor.BlueGray50,
    onError = MaterialColor.White
)

private val LightColorPalette = lightColors(
    primary = Primary,
    primaryVariant = PrimaryVariant,
    secondary = MaterialColor.Orange400,
    secondaryVariant = MaterialColor.Orange600,
    background = MaterialColor.BlueGray50,
    surface = MaterialColor.White,
    error = MaterialColor.Red100,
    onPrimary = MaterialColor.White,
    onSecondary = MaterialColor.White,
    onBackground = MaterialColor.BlueGray800,
    onSurface = MaterialColor.BlueGray800,
    onError = MaterialColor.White
)

@Composable
fun DispenserTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        shapes = Shapes,
        content = content,
    )
}