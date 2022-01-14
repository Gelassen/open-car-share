package com.home.opencarshare.screens.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.home.opencarshare.App
import com.home.opencarshare.R
import com.home.opencarshare.model.pojo.Driver
import com.home.opencarshare.model.pojo.DriverCredentials
import com.home.opencarshare.model.pojo.ServiceMessage
import com.home.opencarshare.model.pojo.Trip
import com.home.opencarshare.network.Repository
import com.home.opencarshare.network.Response
import com.home.opencarshare.persistent.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

sealed interface PassengerTripUiState {
    val isLoading: Boolean
    val errors: List<String>

    data class SearchTripUiState(
        override val isLoading: Boolean = false,
        override val errors: List<String> = emptyList()
    ) : PassengerTripUiState

    data class TripsListUiState(
        val trips: List<Trip> = emptyList(),
        override val isLoading: Boolean = false,
        override val errors: List<String> = emptyList()
    ) : PassengerTripUiState

    data class TripBookUiState(
        val trip: Trip = Trip(),
        val driver: Driver = Driver(),
        override val isLoading: Boolean = false,
        override val errors: List<String> = emptyList()
    ) : PassengerTripUiState
}

enum class TripState {
    SEARCH,
    LIST,
    BOOKING
}

data class PassengerTripState(
    val trips: List<Trip> = emptyList(),
    val bookTrip: Trip = Trip(),
    val driver: Driver = Driver(),
    val isLoading: Boolean = false,
    val errors: List<String> = emptyList(),
    val flag: TripState = TripState.SEARCH
) {
    fun toUiState() : PassengerTripUiState {
        val result: PassengerTripUiState
        when (flag) {
            TripState.SEARCH -> {
                result = PassengerTripUiState.SearchTripUiState(
                    isLoading = isLoading,
                    errors = errors
                )
            }
            TripState.LIST -> {
                result = PassengerTripUiState.TripsListUiState(
                    trips = trips,
                    isLoading = isLoading,
                    errors = errors
                )
            }
            TripState.BOOKING -> {
                result = PassengerTripUiState.TripBookUiState(
                    trip = bookTrip,
                    driver = driver,
                    isLoading = isLoading,
                    errors = errors
                )
            }
        }
        return result
    }
}

@HiltViewModel
class TripsViewModel
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val repo: Repository,
    ) : BaseViewModel(context) {

    private val state = MutableStateFlow(PassengerTripState())
    val uiState = state
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, state.value.toUiState())

    fun errorShown(error: String) {
        state.update { it ->
            Log.d(App.TAG, "[state] errors before update ${it.errors.size}")
            val errors = it.errors.filterNot { it.equals(error) }
            Log.d(App.TAG, "[state] errors after update ${errors.size}")
            it.copy(errors = errors)
        }
    }

    fun getTrips(locationFrom: String, locationTo: String, date: Long) {
        viewModelScope.launch {
            repo.getTrips(locationFrom, locationTo, date)
                .stateIn(viewModelScope)
                .onStart {
                    state.update { state ->
                        state.copy(
                            isLoading = true
                        )
                    }
                }
                .onCompletion {
                    // no op, isLoading will be update in .collect() part
                }
                .catch { e ->
                    Log.e(App.TAG, "Something went wrong on loading trips for $locationFrom and ${Date(date)}", e)
                }
                .collect { it ->
                    when(it) {
                        is Response.Data -> {
                            Log.d(App.TAG, "[state] getTrips update state")
                            state.update { state ->
                                state.copy(flag = TripState.LIST, trips = it.data, isLoading = false)
                            }
                        }
                        is Response.Error.Exception -> {
                            Log.e(App.TAG, "[action] get trips - get an error", it.error)
                            state.update { state ->
                                state.copy(errors = state.errors + getErrorMessage(it), isLoading = false)
                            }
                        }
                        is Response.Error.Message -> {
                            state.update { state ->
                                state.copy(errors = state.errors + getErrorMessage(it), isLoading = false)
                            }
                        }
                        is Response.Loading<*> -> {
                            // no op, operate with special state flag
                        }
                    }
                }
        }
    }

    // TODO expand backend to add driver to the response
    fun getTripById(id: String) {
        Log.d(App.TAG, "[action] getTripById() - start")
        viewModelScope.launch {
            repo.getTripById(id)
                .stateIn(viewModelScope)
                .onStart {
                    state.update { state ->
                        state.copy(isLoading = true)
                    }
                }
                .onCompletion {
                    // no op, isLoading will be updated in .collect() part
                }
                .catch { e ->
                    Log.e(App.TAG, "Something went wrong on loading with id $id", e)
                }
                .collect { it ->
                    Log.d(App.TAG, "[state] getTripById() - collect")
                    when (it) {
                        is Response.Data<Trip> -> {
                            Log.d(App.TAG, "[state] getTripById - update trips state")
                            state.update { state ->
                                state.copy(
                                    flag = TripState.BOOKING,
                                    bookTrip = it.data,
                                    driver = it.data.driver,
                                    isLoading = false)
                            }
                        }
                        is Response.Error.Exception -> {
                            Log.d(App.TAG, "[state] getTripById - update trips error state")
                            state.update { state ->
                                val errors = state.errors + getErrorMessage(it)
                                state.copy(errors = errors, isLoading = false)
                            }
                        }
                        is Response.Error.Message -> {
                            Log.d(App.TAG, "[state] getTripById - update trips error state with message")
                            state.update { state ->
                                val errors = state.errors + getErrorMessage(it)
                                state.copy(errors = errors, isLoading = false)
                            }
                        }
                        is Response.Loading<*> -> {
                            // no op, isLoading will be updated in .collect() and .onStart() points
                        }
                    }
                }
        }
    }
}