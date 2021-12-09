package dev.atick.compose.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.compose.ui.theme.DispenserTheme
import dev.atick.core.ui.BaseComposeFragment
import dev.atick.core.utils.extensions.debugMessage

@AndroidEntryPoint
class DashboardFragment : BaseComposeFragment() {

    private val dashboardFragmentArgs: DashboardFragmentArgs by navArgs()
    private val viewModel: DashboardViewModel by viewModels()

    @Composable
    override fun ComposeUi() {
        DispenserTheme {
            DashboardScreen()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val deviceId = dashboardFragmentArgs.deviceId
        requireContext().debugMessage(deviceId.toString())
        deviceId?.let { id ->
            viewModel.fetchDispenserStates(id)
        }
    }
}