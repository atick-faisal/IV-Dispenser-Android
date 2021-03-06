package dev.atick.compose.ui.dashboard

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.compose.utils.getFloatTimestamp
import dev.atick.core.ui.BaseViewModel
import dev.atick.core.utils.extensions.stateInDelayed
import dev.atick.data.database.room.DispenserDao
import dev.atick.data.models.Command
import dev.atick.data.models.DispenserState
import dev.atick.mqtt.repository.MqttRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class DashboardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dispenserDao: DispenserDao,
    private val mqttRepository: MqttRepository
) : BaseViewModel() {

    private var deviceId: String? = null

    lateinit var lastState: StateFlow<DispenserState>
    lateinit var urineLevel: StateFlow<Float>
    lateinit var urineOutDataset: StateFlow<LineDataSet>
    lateinit var flowRateDataset: StateFlow<LineDataSet>
    lateinit var dripRateDataset: StateFlow<LineDataSet>

    private val _sendingCommand = MutableLiveData<Boolean>()
    val sendingCommand = mutableStateOf(false)

    private val emptyDispenserState = DispenserState(
        deviceId = " --- ",
        room = "101",
        dripRate = 0F,
        flowRate = 0F,
        urineOut = 0F
    )

    init {
        deviceId = savedStateHandle.get<String>("device_id")
        deviceId?.let {
            fetchDispenserStates(it)
        }
    }

    private fun fetchDispenserStates(deviceId: String) {
        val dispenserStates = dispenserDao.getStatesByDeviceId(deviceId, 40)
        viewModelScope.launch {
            lastState = dispenserStates.map {
                if (it.isEmpty()) emptyDispenserState
                else it.first()
            }.stateInDelayed(
                scope = viewModelScope,
                initialValue = emptyDispenserState
            )

            urineLevel = dispenserStates.map {
                if (it.isEmpty()) 0F
                else min(it.first().urineOut / 1000F, 1.0F)
            }.stateInDelayed(
                scope = viewModelScope,
                initialValue = 0F
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
            }.stateInDelayed(
                scope = viewModelScope,
                initialValue = LineDataSet(listOf(), "Drip Rate")
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
            }.stateInDelayed(
                scope = viewModelScope,
                initialValue = LineDataSet(listOf(), "Flow Rate")
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
            }.stateInDelayed(
                scope = viewModelScope,
                initialValue = LineDataSet(listOf(), "Urine Out")
            )
        }
    }

    fun setFlowRate(percent: Float) {
        if (mqttRepository.isClientConnected.value?.peekContent() == true) {
            deviceId?.let { deviceId ->
                sendingCommand()
                mqttRepository.publish(
                    topic = "dev.atick.mqtt/command/${deviceId}",
                    message = Json.encodeToString(
                        Command.serializer(),
                        Command(
                            deviceId = deviceId,
                            flowRate = percent
                        )
                    ),
                    onSuccess = { commandSent() }
                )
            }
        }
    }

    private fun sendingCommand() {
        _sendingCommand.value = true
    }

    fun commandSent() {
        viewModelScope.launch {
            delay(3_000L)
            _sendingCommand.value = false
        }
    }
}