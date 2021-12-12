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
    primary = MaterialColor.Green700,
    primaryVariant = MaterialColor.Green600,
    secondary = MaterialColor.Orange700,
    secondaryVariant = MaterialColor.Orange600,
    background = Color(0xFF_2E3440),
    surface = MaterialColor.BlueGray900,
    error = MaterialColor.Red700,
    onPrimary = MaterialColor.White,
    onSecondary = MaterialColor.White,
    onBackground = MaterialColor.BlueGray50,
    onSurface = MaterialColor.BlueGray50,
    onError = MaterialColor.White
)

private val LightColorPalette = lightColors(
    primary = MaterialColor.Green700,
    primaryVariant = MaterialColor.Green600,
    secondary = MaterialColor.Orange400,
    secondaryVariant = MaterialColor.Orange600,
    background = MaterialColor.BlueGray50,
    surface = MaterialColor.White,
    error = MaterialColor.Red400,
    onPrimary = MaterialColor.White,
    onSecondary = MaterialColor.White,
    onBackground = MaterialColor.BlueGray900,
    onSurface = MaterialColor.BlueGray900,
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

//    val fontFamily = FontFamily(
//        Font(R.font.fira_sans_thin, FontWeight.Thin),
//        Font(R.font.fira_sans_light, FontWeight.Light),
//        Font(R.font.fira_sans_regular, FontWeight.Normal),
//        Font(R.font.fira_sans_medium, FontWeight.Medium),
//        Font(R.font.fira_sans_bold, FontWeight.Bold),
//        Font(R.font.fira_sans_italic, FontWeight.Normal, FontStyle.Italic),
//    )

    MaterialTheme(
        colors = colors,
//        typography = Typography.copy(
//            h1 = TextStyle(
//                color = Color.DarkGray,
//                fontSize = 48.sp,
//                fontFamily = fontFamily,
//                fontWeight = FontWeight.Medium
//            ),
//            h2 = TextStyle(
//                color = Color.DarkGray,
//                fontSize = 32.sp,
//                fontFamily = fontFamily,
//                fontWeight = FontWeight.Medium
//            ),
//            h3 = TextStyle(
//                color = Color.DarkGray,
//                fontSize = 24.sp,
//                fontFamily = fontFamily,
//                fontWeight = FontWeight.Medium
//            ),
//            h4 = TextStyle(
//                color = Color.DarkGray,
//                fontSize = 20.sp,
//                fontFamily = fontFamily,
//                fontWeight = FontWeight.Medium
//            ),
//            h5 = TextStyle(
//                color = Color.DarkGray,
//                fontSize = 16.sp,
//                fontFamily = fontFamily,
//                fontWeight = FontWeight.Normal
//            ),
//            h6 = TextStyle(
//                color = Color.DarkGray,
//                fontSize = 14.sp,
//                fontFamily = fontFamily,
//                fontWeight = FontWeight.Normal
//            )
//        ),
        shapes = Shapes,
        content = content,
    )
}