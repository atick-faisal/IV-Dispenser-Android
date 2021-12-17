package dev.atick.data.database.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.atick.data.models.Login
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

class AppSettingsImpl @Inject constructor(
    private val datastore: DataStore<Preferences>
) : AppSettings {

    companion object {
        val LOGIN_KEY = stringPreferencesKey("dev.atick.dispenser.login")
    }

    private val defaultLogin = Json.encodeToString(
        Login.serializer(),
        Login()
    )

    override val loginCredentials: Flow<Login>
        get() = datastore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            Json.decodeFromString(
                Login.serializer(),
                preferences[LOGIN_KEY] ?: defaultLogin
            )
        }

    override suspend fun saveLoginCredentials(login: Login) {
        datastore.edit { preferences ->
            preferences[LOGIN_KEY] = Json.encodeToString(
                Login.serializer(),
                login
            )
        }
    }
}