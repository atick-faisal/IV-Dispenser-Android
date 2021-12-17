package dev.atick.compose.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.core.ui.BaseViewModel
import dev.atick.core.utils.Event
import dev.atick.data.database.datastore.AppSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    appSettings: AppSettings
) : BaseViewModel() {
    private val _loginStatus = MutableLiveData<Event<Boolean>>()
    val loginStatus: LiveData<Event<Boolean>>
        get() = _loginStatus

    init {
        viewModelScope.launch {
            delay(3000)
            withContext(Dispatchers.IO) {
                appSettings.loginCredentials.collect {
                    _loginStatus.postValue(
                        Event(it.loginStatus)
                    )
                }
            }
        }
    }
}