package dev.atick.compose.ui.splash

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.compose.ui.theme.DispenserTheme
import dev.atick.core.ui.BaseComposeFragment
import dev.atick.core.utils.extensions.observeEvent
import dev.atick.core.utils.extensions.showToast
import dev.atick.data.models.Login
import dev.atick.mqtt.service.MqttService

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
        observeEvent(viewModel.login) { login ->
            if (login.loginStatus) {
                if (viewModel.isClientConnected.value?.peekContent() == true) {
                    navigateToHomeFragment()
                } else {
                    startMqttService(login)
                }
            } else navigateToLoginFragment()
        }

        observeEvent(viewModel.isClientConnected) {
            if (it) navigateToHomeFragment()
            else context?.showToast("Connection Failed!")
        }
    }

    private fun startMqttService(login: Login) {
        val intent = Intent(requireActivity(), MqttService::class.java)
        intent.putExtra("username", login.username)
        intent.putExtra("password", login.password)
        requireContext().startService(intent)
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