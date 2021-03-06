package com.home.opencarshare.screens.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.home.opencarshare.App
import com.home.opencarshare.R
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
import java.lang.IllegalStateException
import javax.inject.Inject

sealed interface CreateTripUiState {
    val isLoading: Boolean
    val errors: List<String>

    data class NoDriverUiState(
        val driver: DriverCredentials = DriverCredentials(),
        override val isLoading: Boolean = false,
        override val errors: List<String> = emptyList()
    ) : CreateTripUiState

    data class DriverHasTripsUiState(
        val driver: DriverCredentials = DriverCredentials(),
        val tripsByDriver: List<Trip> = emptyList(),
        val queueToCancel: Set<String> = emptySet(),
        override val isLoading: Boolean = false,
        override val errors: List<String> = emptyList()
    ) : CreateTripUiState

    data class NewTripUiState(
        val driver: DriverCredentials = DriverCredentials(),
        val tripStatus: TripStatus = TripStatus.NONE,
        val trip: Trip = Trip(),
        override val isLoading: Boolean = false,
        override val errors: List<String> = emptyList()
    ) : CreateTripUiState
}

enum class TripStatus {
    NONE,
    SUCCEED,
    FAILED,
    SUCCEED_WAIT_FOR_ACTION
}

data class CreateTripState(
    val driver: DriverCredentials = DriverCredentials(),
    val tripStatus: TripStatus = TripStatus.NONE,
    val trip: Trip = Trip(),
    val tripsByDriver: List<Trip> = emptyList(),
    val queueToCancel: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errors: List<String> = emptyList()
) {
    fun toUiState() : CreateTripUiState {
        val result: CreateTripUiState
        if (driver.isEmpty()) {
            result = CreateTripUiState.NoDriverUiState(
                driver = driver,
                isLoading = isLoading,
                errors = errors
            )
        } else if (tripStatus == TripStatus.SUCCEED) {
            result = CreateTripUiState.DriverHasTripsUiState(
                driver = driver,
                tripsByDriver = tripsByDriver,
                queueToCancel = queueToCancel,
                isLoading = isLoading,
                errors = errors
            )
        } else {
            result = CreateTripUiState.NewTripUiState(
                driver = driver,
                tripStatus = tripStatus,
                trip = trip,
                isLoading = isLoading,
                errors = errors
            )
        }
        return result
    }
}

const val DAY_IN_MILLIS = 24 * 60 * 60 * 1000L
const val TWO_DAYS_IN_MILLIS = 2 * DAY_IN_MILLIS

@HiltViewModel
class DriverViewModel
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val repo: Repository,
    private val preferenceRepository: PreferenceRepository
    ) : BaseViewModel(context) {

    private val driverPreferencesFlow = preferenceRepository.driverPreferencesFlow

    private val state = MutableStateFlow(CreateTripState())
    val uiState = state
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, state.value.toUiState())

    fun createDriver(driverCredentials: DriverCredentials) {
        viewModelScope.launch {
            preferenceRepository.saveDriver(driverCredentials)
                .flatMapConcat { it ->
                    repo.createDriver(driverCredentials = driverCredentials)
                }
                .flatMapConcat { it ->
                    Log.d(App.TAG, "[state] create driver - get an error")
                    if (it is Response.Error) {
                        flow {
                            Log.d(App.TAG, "[state] create driver - emit an error")
                            emit(it)
                        }.stateIn(viewModelScope)
                    } else {
                        Log.d(App.TAG, "[state] create driver - request driver from server")
                        repo.getDriver(cell = driverCredentials.cell, secret = driverCredentials.secret)
                    }
                }
                .collect { driverResponse ->
                    Log.d(App.TAG, "$driverResponse")
                    processDriverResponse(driverResponse)
                }
        }
    }

    fun getDriver() {
        viewModelScope.launch {
            driverPreferencesFlow
                .stateIn(viewModelScope)
                .flatMapConcat { it ->
                    Log.d(App.TAG, "[get driver] Got driver from cache. Checking is it empty")
                    if (it.driver.isEmpty()) {
                        Log.d(App.TAG, "[get driver] Driver from cache is empty. Emit empty driver credentials")
                        flow {
                            emit(Response.Data(DriverCredentials()))
                        }.stateIn(viewModelScope)
                    } else {
                        Log.d(App.TAG, "[get driver] Driver from cache is not empty. Request driver from server.")
                        repo.getDriver(cell = it.driver.cell, secret = it.driver.secret)
                    }
                }
                .onStart {
                    state.update { state ->
                        state.copy(isLoading = true)
                    }
                }
                .catch { e ->
                    Log.e(App.TAG, "[get driver] Got an exception. Updating the state", e)
                    state.update { state ->
                        val errors = state.errors + getErrorMessage(Response.Error.Exception(e))
                        state.copy(errors = errors, isLoading = false)
                    }
                }
                .collect { it ->
                    processDriverResponse(it)
                }
        }
    }

    fun getDriverWithTrips() {
        viewModelScope.launch {
            driverPreferencesFlow
                .stateIn(viewModelScope)
                .onStart {
                    state.update { state ->
                        state.copy(isLoading = true)
                    }
                }
                .flatMapConcat { it ->
                    if (it.driver.isEmpty()) {
                        flow {
                            emit(Response.Data(DriverCredentials()))
                        }.stateIn(viewModelScope)
                    } else {
                        repo.getDriverWithTrips(cell = it.driver.cell, secret = it.driver.secret)
                    }
                }
                .collect { it ->
                    when(it) {
                        is Response.Data -> {
                            state.update { state ->
                                val twoDaysBeforeNow = System.currentTimeMillis() - TWO_DAYS_IN_MILLIS
                                val tripsWithoutOutdated = it.data.trips.filter { twoDaysBeforeNow < it.date} // filter trips that are outdated
                                val noTrips = tripsWithoutOutdated.isEmpty()
                                state.copy(
                                    driver = it.data,
                                    tripsByDriver = tripsWithoutOutdated,
                                    tripStatus = if (noTrips) { TripStatus.NONE } else { TripStatus.SUCCEED },
                                    isLoading = false
                                )
                            }
                        }
                        is Response.Error -> {
                            state.update { state ->
                                state.copy(errors = state.errors + getErrorMessage(it), isLoading = false)
                            }
                        }
                        is Response.Loading<*> -> {
                            // no op, isLoading will be updated in .collect() and .onStart() points
                        }
                    }
                }
        }
    }

    private fun processDriverResponse(driverResponse: Response<DriverCredentials>) {
        when (driverResponse) {
            is Response.Data -> {
                state.update { state ->
                    state.copy(driver = driverResponse.data, isLoading = false)
                }
            }
            is Response.Error -> {
                state.update { state ->
                    val errors = state.errors + getErrorMessage(driverResponse)
                    state.copy(errors = errors, isLoading = false)
                }
            }
            is Response.Loading<*> -> {
                Log.d(App.TAG, "process response ${driverResponse.isLoading}")
            }
        }
    }

    fun errorShown(error: String) {
        state.update { it ->
            val errors = it.errors.filterNot { it.equals(error) }
            it.copy(errors = errors)
        }
    }

    fun createTrip(trip: Trip) {
        viewModelScope.launch {
            Log.d(App.TAG, "[action] create trip start")
            repo.createTrip(trip)
                .stateIn(viewModelScope)
                .onStart {
                    state.update { state ->
                        state.copy(isLoading = true)
                    }
                }
                .catch { ex ->
                    Log.e(App.TAG, "Something went wrong with creating the trip $trip", ex)
                    state.update { state ->
                        val errors = state.errors + getErrorMessage(Response.Error.Exception(ex))
                        state.copy(errors = errors, isLoading = false)
                    }
                }
                .collect { it ->
                    Log.d(App.TAG, "[action] create trip collect data")
                    when (it) {
                        is Response.Data -> {
                            Log.d(App.TAG, "[action] create trip data as Response.Data")
                            state.update { state ->
                                var tripStatus = TripStatus.NONE
                                var errorMessage = ""
                                when (it.data.status) {
                                    ServiceMessage.Status.NONE -> {
                                        tripStatus = TripStatus.NONE
                                    }
                                    ServiceMessage.Status.SUCCEED -> {
                                        tripStatus = TripStatus.SUCCEED_WAIT_FOR_ACTION
                                    }
                                    ServiceMessage.Status.FAILED -> {
                                        tripStatus = TripStatus.FAILED
                                        errorMessage = it.data.message
                                    }
                                }
                                if (errorMessage.isEmpty()) {
                                    state.copy(tripStatus = tripStatus, isLoading = false)
                                } else {
                                    state.copy(tripStatus = tripStatus, errors = state.errors + errorMessage, isLoading = false)
                                }
                            }
                        }
                        is Response.Error.Exception -> {
                            Log.d(App.TAG, "[action] create trip data as Response.Error.Exception")
                            state.update { state ->
                                val errors = state.errors + getErrorMessage(it)
                                state.copy(errors = errors, isLoading = false)
                            }
                        }
                        is Response.Error.Message -> {
                            Log.d(App.TAG, "[action] create trip data as Response.Error.Message")
                            state.update { state ->
                                val errors = state.errors + getErrorMessage(it)
                                state.copy(errors = errors, isLoading = false)
                            }
                        }
                        is Response.Loading<*> -> {
                            Log.d(App.TAG, "[action] create trip data as Response.Loading")
//                            state.update { state ->
//                                state.copy(isLoading = true)
//                            }
                        }
                    }
                }
        }
    }

    fun onAfterTripCreateAction() {
        state.update { state ->
            state.copy(tripStatus = TripStatus.SUCCEED)
        }
    }

    @Deprecated("Redundent. Use getDriverWithTrips() instead")
    fun getInitialState() {
        viewModelScope.launch {
            driverPreferencesFlow
                .stateIn(viewModelScope)
                .flatMapConcat { it ->
                    if (it.driver.isEmpty()) {
                        state.update { state ->
                            state.copy(driver = DriverCredentials(), isLoading = false)
                        }
                        flow {
                            emit(Response.Data(DriverCredentials()))
                        }.stateIn(viewModelScope)
                    } else {
                        repo.getDriver(cell = it.driver.cell, secret = it.driver.secret)
                    }
                }
                .flatMapConcat { it ->
                    if (it is Response.Data && !it.data.isEmpty()) {
                        Log.d(App.TAG, "[getTripsByDriver] get driver from server, request trips")
                        var driver = it.data
                        state.update { state ->
                            state.copy(driver = driver)
                        }
                        repo.getTripsByDriver(cell = driver.cell, secret = driver.secret)
                    } else {
                        Log.d(App.TAG, "[getTripsByDriver] there is an error with driver from server, emit error")
                        flow {
                            emit(Response.Data<List<Trip>>(emptyList()))
                        }.stateIn(viewModelScope)
                    }
                }
                .collect { it ->
                    when (it) {
                        is Response.Data -> {
                            Log.d(App.TAG, "[getTripsByDriver] got data as Response.Data")
                            state.update { state ->
                                val twoDaysBeforeNow = System.currentTimeMillis() - TWO_DAYS_IN_MILLIS
                                val tripsWithoutOutdated = it.data.filter { twoDaysBeforeNow < it.date} // filter trips that are outdated
                                val noTrips = tripsWithoutOutdated.isEmpty()
                                state.copy(
                                    tripsByDriver = tripsWithoutOutdated,
                                    tripStatus = if (noTrips) { TripStatus.NONE } else { TripStatus.SUCCEED },
                                    isLoading = false
                                )
                            }
                        }
                        is Response.Error -> {
                            Log.d(App.TAG, "[getTripsByDriver] got data as Response.Error")
                            val errorMessage = getErrorMessage(it)
                            state.update { state ->
                                state.copy(errors = state.errors + errorMessage, isLoading = false)
                            }
                        }
                    }
                }
        }
    }

    fun getTripsByDriver() {
        viewModelScope.launch {
            Log.d(App.TAG, "[action] get trips by driver start")
            driverPreferencesFlow
                .stateIn(viewModelScope)
                .flatMapConcat { it ->
                    if (it.driver.isEmpty()) {
                        Log.d(App.TAG, "[getTripsByDriver] check driver in cache and it is empty")
                        flow {
                            emit(Response.Data(DriverCredentials()))
                        }.stateIn(viewModelScope)
                    } else {
                        Log.d(App.TAG, "[getTripsByDriver] check driver in cache and it isn't empty. Request full profile from server")
                        repo.getDriver(cell = it.driver.cell, secret = it.driver.secret)
                    }
                }
                .flatMapConcat { it ->
                    if (it is Response.Data && !it.data.isEmpty()) {
                        Log.d(App.TAG, "[getTripsByDriver] get driver from server, request trips")
                        var driver = it.data
                        repo.getTripsByDriver(cell = driver.cell, secret = driver.secret)
                    } else {
                        Log.d(App.TAG, "[getTripsByDriver] there is an error with driver from server, emit error")
                        flow {
                            emit(Response.Error.Message("There is no driver for call for trips"))
                        }.stateIn(viewModelScope)
                    }
                }
                .collect { it ->
                    Log.d(App.TAG, "[getTripsByDriver] collect data")
                    when (it) {
                        is Response.Data -> {
                            Log.d(App.TAG, "[getTripsByDriver] got data as Response.Data")
                            state.update { state ->
                                val twoDaysBeforeNow = System.currentTimeMillis() - TWO_DAYS_IN_MILLIS
                                val tripsWithoutOutdated = it.data.filter { twoDaysBeforeNow < it.date} // filter trips that are outdated
                                val noTrips = tripsWithoutOutdated.isEmpty()
                                Log.d(App.TAG, "$tripsWithoutOutdated")
                                state.copy(
                                    tripsByDriver = tripsWithoutOutdated,
                                    tripStatus = if (noTrips) { TripStatus.NONE } else { TripStatus.SUCCEED },
                                    isLoading = false
                                )
                            }
                        }
                        is Response.Error -> {
                            Log.d(App.TAG, "[getTripsByDriver] got data as Response.Error")
                            val errorMessage = getErrorMessage(it)
                            state.update { state ->
                                state.copy(errors = state.errors + errorMessage, isLoading = false)
                            }
                        }
                    }
                }
        }
    }

    fun addTripInCancelList(tripId: String) {
        state.update { state ->
            state.copy(queueToCancel = state.queueToCancel + tripId)
        }
    }

    fun removeTripFromCancelList(tripId: String) {
        state.update { state ->
            state.copy(queueToCancel = state.queueToCancel.minus(tripId))
        }
    }

    fun cancelTrip(tripId: String, driver: DriverCredentials) {
        viewModelScope.launch {
            repo.cancelTrip(tripId, driver.cell, driver.secret)
                .stateIn(viewModelScope)
                .onStart {
                    state.update { state ->
                        state.copy(isLoading = true)
                    }
                }
                .collect { it ->
                    when (it) {
                        is Response.Data -> {
                            state.update { state ->
                                val newTripsList = state.tripsByDriver.filter { it.id != tripId }
                                if (newTripsList.isNotEmpty()) {
                                    state.copy(
                                        queueToCancel = state.queueToCancel.minus(tripId),
                                        tripsByDriver = newTripsList,
                                        isLoading = false
                                    )
                                } else {
                                    state.copy(
                                        tripStatus = TripStatus.NONE,
                                        queueToCancel = state.queueToCancel.minus(tripId),
                                        tripsByDriver = newTripsList,
                                        isLoading = false
                                    )
                                }

                            }.also {
                                Log.d(App.TAG, "[state] $state")
                            }
                        }
                        is Response.Error -> {
                            state.update { state ->
                                state.copy(errors = state.errors + getErrorMessage(it), isLoading = false)
                            }
                        }
                    }
                }
        }
    }

}