package dev.atick.compose.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.compose.utils.getFloatTimestamp
import dev.atick.data.database.room.DispenserDao
import dev.atick.data.models.DispenserState
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class DashboardViewModel @Inject constructor(private val dispenserDao: DispenserDao) : ViewModel() {

    lateinit var dispenserStates: LiveData<List<DispenserState>>
    lateinit var lastState: LiveData<DispenserState>
    lateinit var urineLevel: LiveData<Float>
    lateinit var urineOutDataset: LiveData<LineDataSet>
    lateinit var flowRateDataset: LiveData<LineDataSet>
    lateinit var dripRateDataset: LiveData<LineDataSet>

    fun fetchDispenserStates(deviceId: String) {
        dispenserStates = dispenserDao.getStatesByDeviceId(deviceId, 40)
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
        urineLevel = Transformations.map(dispenserStates) {
            if (it.isEmpty()) 0F
            else min(it.first().urineOut / 1000F, 1.0F)
        }
        dripRateDataset = Transformations.map(dispenserStates) { dispenserStates ->
            val entries = mutableListOf<Entry>()
            if (dispenserStates.isEmpty()) LineDataSet(entries, "Drip Rate")
            else {
                dispenserStates.reversed().forEachIndexed { _, dispenserState ->
                    entries.add(
                        Entry(
                            getFloatTimestamp(dispenserState.timestamp),
                            dispenserState.dripRate ?: 0F
                        )
                    )
                }
                LineDataSet(entries, "Drip Rate")
            }
        }
        flowRateDataset = Transformations.map(dispenserStates) { flowRateDataset ->
            val entries = mutableListOf<Entry>()
            if (flowRateDataset.isEmpty()) LineDataSet(entries, "Flow Rate")
            else {
                flowRateDataset.reversed().forEach { dispenserState ->
                    entries.add(
                        Entry(
                            getFloatTimestamp(dispenserState.timestamp),
                            dispenserState.flowRate
                        )
                    )
                }
                LineDataSet(entries, "Flow Rate")
            }
        }
        urineOutDataset = Transformations.map(dispenserStates) { dispenserStates ->
            val entries = mutableListOf<Entry>()
            if (dispenserStates.isEmpty()) LineDataSet(entries, "Urine Out")
            else {
                dispenserStates.reversed().forEach { dispenserState ->
                    entries.add(
                        Entry(
                            getFloatTimestamp(dispenserState.timestamp),
                            dispenserState.urineOut
                        )
                    )
                }
                LineDataSet(entries, "Urine Out")
            }
        }
    }
}