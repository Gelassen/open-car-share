package com.home.opencarshare.persistent

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.home.opencarshare.App
import com.home.opencarshare.App.TAG
import com.home.opencarshare.converters.DriverToJsonConverter
import com.home.opencarshare.model.pojo.Driver
import com.home.opencarshare.model.pojo.DriverCredentials
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.lang.RuntimeException
import javax.inject.Inject

/**
 * There are several ways how to deal with single instance of DataStore
 * @link https://stackoverflow.com/questions/66669724/how-to-use-android-datastore-with-multi-users-or-files
 * */

private val Context.dataStore by preferencesDataStore(PreferenceRepository.PrefKeys.KEY_DATA_STORE)

data class DriverPreferences(val driver: DriverCredentials)

class PreferenceRepository
@Inject constructor(@ApplicationContext private val context: Context) {

    object PrefKeys {
        val KEY_DATA_STORE = "profile_datastore"
        val KEY_DRIVER_PROFILE = stringPreferencesKey("KEY_DRIVER_PROFILE")
    }

    // TODO prevent two instance of DataStore, e.g. after activity rotation

    private val dataStore: DataStore<Preferences> = context.dataStore

    val driverPreferencesFlow: Flow<DriverPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val driver = DriverToJsonConverter().convertJsonToDriver(preferences[PrefKeys.KEY_DRIVER_PROFILE])
            DriverPreferences(driver)
        }
        .distinctUntilChanged() // TODO check it

    suspend fun saveDriver(driver: DriverCredentials): Flow<DriverCredentials> {
        return flow {
            dataStore.edit { preferences ->
                preferences[PrefKeys.KEY_DRIVER_PROFILE] = DriverToJsonConverter().convertDriverToJson(driver)
            }
            emit(driver)
        }
    }
}