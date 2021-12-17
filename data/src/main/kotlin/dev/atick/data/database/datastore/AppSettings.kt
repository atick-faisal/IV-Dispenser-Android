package dev.atick.data.database.datastore

import dev.atick.data.models.Login
import kotlinx.coroutines.flow.Flow

interface AppSettings {
    val loginCredentials: Flow<Login>
    suspend fun saveLoginCredentials(login: Login)
}