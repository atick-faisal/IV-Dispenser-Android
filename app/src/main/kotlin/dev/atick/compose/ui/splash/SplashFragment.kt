package dev.atick.compose.ui.splash

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.compose.ui.theme.DispenserTheme
import dev.atick.core.ui.BaseComposeFragment
import dev.atick.core.utils.extensions.observeEvent

@AndroidEntryPoint
class SplashFragment : BaseComposeFragment() {

    private val viewModel: SplashViewModel by viewModels()

    @Composable
    override fun ComposeUi() {
        DispenserTheme {
            SplashScreen()
        }
    }

    override fun observeStates() {
        super.observeStates()
        observeEvent(viewModel.loginStatus) { userLoggedIn ->
            if (userLoggedIn) navigateToHomeFragment()
            else navigateToLoginFragment()
        }
    }

    private fun navigateToLoginFragment() {
        findNavController().navigate(
            SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        )
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(
            SplashFragmentDirections.actionSplashFragmentToHomeFragment()
        )
    }
}