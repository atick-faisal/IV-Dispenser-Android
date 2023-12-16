package dev.atick.compose.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.core.ui.BaseViewModel
import dev.atick.core.utils.Event
import dev.atick.data.database.datastore.AppSettings
import dev.atick.data.models.Login
import dev.atick.mqtt.repository.MqttRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    appSettings: AppSettings,
    mqttRepository: MqttRepository
) : BaseViewModel() {
    private val _login = MutableLiveData<Event<Login>>()
    val login: LiveData<Event<Login>>
        get() = _login

    val isClientConnected = mqttRepository.isClientConnected

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appSettings.loginCredentials.collect {
                    _login.postValue(
                        Event(it)
                    )
                }
            }
        }
    }
}