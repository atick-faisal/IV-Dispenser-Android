package dev.atick.compose.ui.dashboard.components

import android.animation.ValueAnimator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.LottieAnimationView
import dev.atick.compose.R
import dev.atick.core.utils.extensions.round

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun BottomMenuContent(
    modifier: Modifier = Modifier,
    flowPercentage: Float,
    onValueChange: (Float) -> Unit,
    onFinalValueChange: (Float) -> Unit,
    isLoaderVisible: Boolean,
    onCancel: () -> Unit
) {
    Row(
        modifier = modifier.padding(
            top = 32.dp,
            bottom = 64.dp,
            start = 32.dp,
            end = 32.dp
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Knob(
            modifier = Modifier
                .width(120.dp)
                .height(120.dp),
            percent = flowPercentage,
            onValueChange = onValueChange,
            onFinalValue = onFinalValueChange
        )

        Spacer(modifier = Modifier.width(32.dp))

        Column(Modifier.fillMaxWidth()) {
            Text(
                text = "Flow Rate",
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${(flowPercentage * 100F).round()} mL/h",
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(isLoaderVisible) {
                Column {
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
                            .height(32.dp)
                            .fillMaxWidth()
                    )

                    OutlinedButton(onClick = onCancel) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }
}