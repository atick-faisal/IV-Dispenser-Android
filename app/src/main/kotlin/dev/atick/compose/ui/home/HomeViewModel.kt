package dev.atick.compose.ui.home

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.core.utils.extensions.debugMessage
import dev.atick.mqtt.repository.MqttRepository
import dev.atick.mqtt.service.MqttService
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {


}