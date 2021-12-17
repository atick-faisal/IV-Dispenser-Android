package dev.atick.compose.ui.splash

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.BaseComposeFragment

@AndroidEntryPoint
class SplashFragment : BaseComposeFragment() {
    @Composable
    override fun ComposeUi() {
        SplashScreen()
    }
}