package dev.atick.compose.ui.dashboard

import androidx.lifecycle.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.compose.utils.getFloatTimestamp
import dev.atick.core.ui.BaseViewModel
import dev.atick.core.utils.Event
import dev.atick.data.database.room.DispenserDao
import dev.atick.data.models.DispenserState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class DashboardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dispenserDao: DispenserDao
) : BaseViewModel() {

    lateinit var lastState: StateFlow<DispenserState>
    lateinit var urineLevel: StateFlow<Float>
    lateinit var urineOutDataset: StateFlow<LineDataSet>
    lateinit var flowRateDataset: StateFlow<LineDataSet>
    lateinit var dripRateDataset: StateFlow<LineDataSet>

    private val _flowRate = MutableLiveData<Event<Float>>()
    val flowRate: LiveData<Event<Float>>
        get() = _flowRate

    private val _sendingCommand = MutableLiveData<Boolean>()
    val sendingCommand: LiveData<Boolean>
        get() = _sendingCommand

    private val emptyDispenserState = DispenserState(
        deviceId = " --- ",
        room = "101",
        dripRate = 0F,
        flowRate = 0F,
        urineOut = 0F
    )

    init {
        savedStateHandle.get<String>("device_id")?.let {
            fetchDispenserStates(it)
        }
    }

    private fun fetchDispenserStates(deviceId: String) {
        val dispenserStates = dispenserDao.getStatesByDeviceId(deviceId, 40)
        viewModelScope.launch {
            lastState = dispenserStates.map {
                if (it.isEmpty()) emptyDispenserState
                else it.first()
            }.stateIn(
                scope = viewModelScope,
                initialValue = emptyDispenserState,
                started = SharingStarted.WhileSubscribed(5000)
            )

            urineLevel = dispenserStates.map {
                if (it.isEmpty()) 0F
                else min(it.first().urineOut / 1000F, 1.0F)
            }.stateIn(
                scope = viewModelScope,
                initialValue = 0F,
                started = SharingStarted.WhileSubscribed(5000)
            )

            dripRateDataset = dispenserStates.map { dispenserStates ->
                val entries = mutableListOf<Entry>()
                dispenserStates.reversed().forEachIndexed { _, dispenserState ->
                    entries.add(
                        Entry(
                            getFloatTimestamp(dispenserState.timestamp),
                            dispenserState.dripRate ?: 0F
                        )
                    )
                }
                LineDataSet(entries, "Drip Rate")
            }.stateIn(
                scope = viewModelScope,
                initialValue = LineDataSet(listOf(), "Drip Rate"),
                started = SharingStarted.WhileSubscribed(5000)
            )

            flowRateDataset = dispenserStates.map { dispenserStates ->
                val entries = mutableListOf<Entry>()
                dispenserStates.reversed().forEach { dispenserState ->
                    entries.add(
                        Entry(
                            getFloatTimestamp(dispenserState.timestamp),
                            dispenserState.flowRate
                        )
                    )
                }
                LineDataSet(entries, "Flow Rate")
            }.stateIn(
                scope = viewModelScope,
                initialValue = LineDataSet(listOf(), "Flow Rate"),
                started = SharingStarted.WhileSubscribed(5000)
            )

            urineOutDataset = dispenserStates.map { dispenserStates ->
                val entries = mutableListOf<Entry>()
                dispenserStates.reversed().forEach { dispenserState ->
                    entries.add(
                        Entry(
                            getFloatTimestamp(dispenserState.timestamp),
                            dispenserState.urineOut
                        )
                    )
                }
                LineDataSet(entries, "Urine Out")
            }.stateIn(
                scope = viewModelScope,
                initialValue = LineDataSet(listOf(), "Urine Out"),
                started = SharingStarted.WhileSubscribed(5000)
            )
        }
    }

    fun setFlowRate(percent: Float) {
        _flowRate.value = Event(percent)
    }

    fun sendingCommand() {
        _sendingCommand.value = true
    }

    fun commandSent() {
        viewModelScope.launch {
            delay(3000)
            _sendingCommand.value = false
        }
    }
}