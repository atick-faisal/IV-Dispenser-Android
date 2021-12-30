package dev.atick.compose.ui.login

import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.compose.ui.theme.DispenserTheme
import dev.atick.core.ui.BaseComposeFragment
import dev.atick.core.utils.extensions.debugMessage
import dev.atick.core.utils.extensions.observeEvent
import dev.atick.data.models.Login
import dev.atick.mqtt.repository.MqttRepository
import dev.atick.mqtt.service.MqttService
import javax.inject.Inject

@ExperimentalAnimationApi
@AndroidEntryPoint
class LoginFragment : BaseComposeFragment() {

    @Inject
    lateinit var mqttRepository: MqttRepository

    private val viewModel: LoginViewModel by viewModels()

    @Composable
    override fun ComposeUi() {
        DispenserTheme {
            LoginScreen(::login)
        }
    }

    override fun observeStates() {
        super.observeStates()
        observeEvent(mqttRepository.isClientConnected) {
            if (it) {
                context?.debugMessage("Connected Successfully")
                viewModel.saveLoginCredentials()
                navigateToHomeFragment()
            } else {
                context?.debugMessage("Connection Failed")
            }
            viewModel.endLoginProcess()
        }
    }

    private fun login(login: Login) {
        viewModel.startLoginProcess()
        startMqttService(login)
    }

    private fun startMqttService(login: Login) {
        val intent = Intent(requireActivity(), MqttService::class.java)
        intent.putExtra("username", login.username)
        intent.putExtra("password", login.password)
        requireContext().startService(intent)
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        )
    }
}