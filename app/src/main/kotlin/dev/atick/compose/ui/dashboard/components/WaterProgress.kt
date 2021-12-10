package dev.atick.compose.ui.dashboard.components

import android.animation.ValueAnimator
import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import dev.atick.compose.R

@Composable
fun WaterProgress(
    title: String,
    value: Float,
    progress: Float,
    modifier: Modifier = Modifier
) {



    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { ctx ->
                LottieAnimationView(ctx).apply {
                    setAnimation(R.raw.urine_out)
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = LottieDrawable.REVERSE
                    speed = 0.1F
                    playAnimation()
                }
            },
            update = {
                it.setMinAndMaxFrame(
                    if (progress <= 0.05F) 0
                    else (226F * (progress - 0.05F)).toInt(),
                    if (progress >= 0.95F) 226
                    else (226F * (progress + 0.05F)).toInt()
                )
            },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                color = MaterialTheme.colors.onSurface
            )
            Text(
                text = "$value mL",
                fontSize = 24.sp,
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}