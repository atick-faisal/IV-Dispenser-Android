package dev.atick.compose.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.data.database.room.DispenserDao
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(dispenserDao: DispenserDao) : ViewModel() {
    val dispensers = dispenserDao.getAllDispensers()
}