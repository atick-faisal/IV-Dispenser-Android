package dev.atick.compose.ui.dashboard.components

import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import kotlin.math.PI
import kotlin.math.atan2
import dev.atick.compose.R

@ExperimentalComposeUiApi
@Composable
fun Knob(
    modifier: Modifier = Modifier,
    limitingAngle: Float = 25f,
    percent: Float,
    onValueChange: (Float) -> Unit,
    onFinalValue: (Float) -> Unit
) {
    var rotation by remember {
        mutableStateOf(percent * (360F - 2 * limitingAngle) + limitingAngle)
    }
    var newPercentage by remember {
        mutableStateOf(0F)
    }

    var touchX by remember {
        mutableStateOf(0f)
    }
    var touchY by remember {
        mutableStateOf(0f)
    }
    var centerX by remember {
        mutableStateOf(0f)
    }
    var centerY by remember {
        mutableStateOf(0f)
    }

    Image(
        painter = painterResource(id = R.drawable.ic_knob),
        contentDescription = "Music knob",
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                val windowBounds = it.boundsInWindow()
                centerX = windowBounds.size.width / 2f
                centerY = windowBounds.size.height / 2f
            }
            .pointerInteropFilter { event ->
                touchX = event.x
                touchY = event.y
                val angle = -atan2(centerX - touchX, centerY - touchY) * (180f / PI).toFloat()

                when (event.action) {
                    MotionEvent.ACTION_DOWN,
                    MotionEvent.ACTION_MOVE -> {
                        if (angle !in -limitingAngle..limitingAngle) {
                            val fixedAngle = if (angle in -180f..-limitingAngle) {
                                360f + angle
                            } else {
                                angle
                            }
                            rotation = fixedAngle

                            newPercentage =
                                (fixedAngle - limitingAngle) / (360f - 2 * limitingAngle)
                            onValueChange(newPercentage)
                            true
                        } else false
                    }
                    MotionEvent.ACTION_UP -> {
                        onFinalValue(newPercentage)
                        true
                    }
                    else -> false
                }
            }
            .rotate(rotation)
    )
}