package dev.atick.compose.ui.login

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
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

@ExperimentalAnimationApi
@AndroidEntryPoint
class LoginFragment : BaseComposeFragment() {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var mqttService: MqttService
    private lateinit var mqttRepository: MqttRepository
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MqttService.LocalBinder
            mqttService = binder.getService()
            mqttRepository = mqttService
            mBound = true

            this@LoginFragment.observeEvent(mqttService.isClientConnected) {
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

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    @Composable
    override fun ComposeUi() {
        DispenserTheme {
            LoginScreen(::login)
        }
    }

    private fun login(login: Login) {
        viewModel.startLoginProcess()
        startMqttService(login)
        bindMqttService()
    }

    private fun startMqttService(login: Login) {
        val intent = Intent(requireActivity(), MqttService::class.java)
        intent.putExtra("username", login.username)
        intent.putExtra("password", login.password)
        requireContext().startService(intent)
    }

    private fun bindMqttService() {
        Intent(requireContext(), MqttService::class.java).also { intent ->
            activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        )
    }
}