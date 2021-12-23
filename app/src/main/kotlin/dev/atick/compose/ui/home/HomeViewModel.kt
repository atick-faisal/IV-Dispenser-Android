package dev.atick.compose.ui.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.core.ui.BaseViewModel
import dev.atick.data.database.room.DispenserDao
import dev.atick.data.models.Dispenser
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    dispenserDao: DispenserDao
) : BaseViewModel() {
    val dispensers: StateFlow<List<Dispenser>> =
        dispenserDao
            .getAllDispensers()
            .stateIn(
                initialValue = listOf(),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )
}