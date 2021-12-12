package dev.atick.compose.ui.login

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.compose.ui.theme.DispenserTheme
import dev.atick.core.ui.BaseComposeFragment
import dev.atick.core.utils.extensions.observeEvent

@ExperimentalAnimationApi
@AndroidEntryPoint
class LoginFragment : BaseComposeFragment() {

    private val viewModel: LoginViewModel by viewModels()

    @Composable
    override fun ComposeUi() {
        DispenserTheme {
            LoginScreen()
        }
    }

    override fun observeStates() {
        super.observeStates()
        observeEvent(viewModel.login) {
            if (it) {
                navigateToHomeFragment()
            }
        }
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        )
    }
}