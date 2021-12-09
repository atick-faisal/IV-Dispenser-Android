package dev.atick.compose.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.data.database.room.DispenserDao
import dev.atick.data.models.DispenserState
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class DashboardViewModel @Inject constructor(private val dispenserDao: DispenserDao) : ViewModel() {

    private lateinit var dispenserStates: LiveData<List<DispenserState>>
    lateinit var lastState: LiveData<DispenserState>
    lateinit var urinePercentage: LiveData<Float>

    fun fetchDispenserStates(deviceId: String) {
        dispenserStates = dispenserDao.getStatesByDeviceId(deviceId)
        lastState = Transformations.map(dispenserStates) {
            if (it.isEmpty()) {
                DispenserState(
                    deviceId = " --- ",
                    room = "101",
                    dripRate = 0F,
                    flowRate = 0F,
                    urineOut = 0F
                )
            } else it.first()
        }
        urinePercentage = Transformations.map(dispenserStates) {
            if (it.isEmpty()) 0F
            else min(it.first().urineOut / 1000F, 1.0F)
        }
    }
}