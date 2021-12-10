package com.home.opencarshare.persistent

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.home.opencarshare.App.TAG
import com.home.opencarshare.converters.DriverToJsonConverter
import com.home.opencarshare.model.pojo.Driver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

data class DriverPreferences(val driver: Driver)

class PreferenceRepository
@Inject constructor(@ApplicationContext private val context: Context) {

    object PrefKeys {
        val KEY_DATA_STORE = "profile_datastore"
        val KEY_DRIVER_PROFILE = stringPreferencesKey("KEY_DRIVER_PROFILE")
    }

    private val Context.dataStore by preferencesDataStore(PrefKeys.KEY_DATA_STORE)

    private val dataStore: DataStore<Preferences> = context.dataStore

    val driverPreferencesFlow: Flow<DriverPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val driver = DriverToJsonConverter().convertJsonToDriver(preferences[PrefKeys.KEY_DRIVER_PROFILE])
            DriverPreferences(driver)
        }

    suspend fun saveDriver(driver: Driver) {
        dataStore.edit { preferences ->
            preferences[PrefKeys.KEY_DRIVER_PROFILE] = DriverToJsonConverter().convertDriverToJson(driver)
        }
    }
}