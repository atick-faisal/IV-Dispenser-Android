package dev.atick.compose.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.core.utils.Event
import dev.atick.data.database.room.DispenserDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    val loginProgress = mutableStateOf(false)
    private val _login = MutableLiveData<Event<Boolean>>()
    val login: LiveData<Event<Boolean>>
        get() = _login

    fun login() {
        viewModelScope.launch {
            loginProgress.value = true
            delay(3000)
            loginProgress.value = false
            _login.value = Event(true)
        }
    }
}