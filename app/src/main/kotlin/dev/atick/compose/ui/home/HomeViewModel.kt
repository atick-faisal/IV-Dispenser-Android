package dev.atick.compose.ui.home

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.core.ui.BaseViewModel
import dev.atick.data.database.room.DispenserDao
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    dispenserDao: DispenserDao
) : BaseViewModel() {
    val dispensers = dispenserDao.getAllDispensers()
}