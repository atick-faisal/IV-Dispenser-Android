package dev.atick.compose.ui.login

import android.animation.ValueAnimator
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.LottieAnimationView
import dev.atick.compose.R
import dev.atick.compose.ui.login.components.LoginTextFiled
import dev.atick.data.models.Login

@ExperimentalAnimationApi
@Composable
fun LoginScreen(
    onLoginClick: (Login) -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    return Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AndroidView(
                factory = { ctx ->
                    LottieAnimationView(ctx).apply {
                        setAnimation(R.raw.server_alt)
                        repeatCount = ValueAnimator.INFINITE
                        speed = 0.5F
                    }
                },
                update = {
                    if (viewModel.loginProgress.value) {
                        it.playAnimation()
                    } else {
                        it.pauseAnimation()
                    }
                },
                modifier = Modifier
                    //.width(64.dp)
                    .fillMaxHeight(0.12F)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                text = "Please Login to the Hospital's Server",
                color = MaterialTheme.colors.onSurface,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            LoginTextFiled(
                textFieldValue = viewModel.username.value,
                labelResourceId = R.string.label_username,
                hintResourceId = R.string.hint_username,
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Person, contentDescription = "")
                },
                onValueChange = {
                    viewModel.username.value = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            LoginTextFiled(
                textFieldValue = viewModel.password.value,
                isPasswordField = true,
                labelResourceId = R.string.label_password,
                hintResourceId = R.string.hint_password,
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Password, contentDescription = "")
                },
                onValueChange = {
                    viewModel.password.value = it
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    onLoginClick(
                        Login(
                            username = viewModel.username.value,
                            password = viewModel.password.value
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = stringResource(R.string.login))
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.4F))
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.width(120.dp),
                painter = painterResource(id = R.drawable.qu_logo),
                contentDescription = "Qatar University Logo"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Qatar University\nMachine Learning Group",
                color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}