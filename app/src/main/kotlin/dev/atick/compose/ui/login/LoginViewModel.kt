package dev.atick.compose.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.core.ui.BaseViewModel
import dev.atick.data.database.datastore.AppSettings
import dev.atick.data.models.Login
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val appSettings: AppSettings
) : BaseViewModel() {
    val loginProgress = mutableStateOf(false)
    val username = mutableStateOf("")
    val password = mutableStateOf("")

    fun saveLoginCredentials() {
        viewModelScope.launch {
            appSettings.saveLoginCredentials(
                Login(
                    username = username.value,
                    password = password.value,
                    loginStatus = true
                )
            )
        }
    }

    fun startLoginProcess() {
        loginProgress.value = true
    }

    fun endLoginProcess() {
        loginProgress.value = false
    }
}