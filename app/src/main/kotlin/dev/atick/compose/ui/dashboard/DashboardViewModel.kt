package dev.atick.compose.ui.dashboard

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.data.database.room.DispenserDao
import dev.atick.data.models.DispenserState
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val dispenserDao: DispenserDao) : ViewModel() {

    lateinit var dispenserStates: LiveData<List<DispenserState>>
    val lastState = mutableStateOf(
        DispenserState(
            deviceId = " --- ",
            room = "101",
            flowRate = 0F,
            dripRate = 0F,
            urineOut = 0F
        )
    )

    fun fetchDispenserStates(deviceId: String) {
        dispenserStates = dispenserDao.getStatesByDeviceId(deviceId)
        dispenserStates.value?.let { states ->
            if (states.isNotEmpty()) {
                lastState.value = states.first()
            }
        }
    }
}