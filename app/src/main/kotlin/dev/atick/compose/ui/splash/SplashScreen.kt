package dev.atick.compose.ui.splash

import android.animation.ValueAnimator
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.LottieAnimationView
import dev.atick.compose.R

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
        ,
        contentAlignment = Alignment.Center
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.ic_splash_icon),
                contentDescription = "",
                modifier = Modifier
                    .width(160.dp)
                    .height(160.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            AndroidView(
                factory = { ctx ->
                    LottieAnimationView(ctx).apply {
                        setAnimation(R.raw.loader)
                        repeatCount = ValueAnimator.INFINITE
                        enableMergePathsForKitKatAndAbove(true)
                        playAnimation()
                    }
                },
                modifier = Modifier
                    .width(160.dp)
                    .height(8.dp)
                    .fillMaxWidth()
            )
        }
    }
}