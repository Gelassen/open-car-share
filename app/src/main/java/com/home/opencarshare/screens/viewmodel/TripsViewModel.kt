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

@HiltViewModel
class TripsViewModel
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val repo: Repository,
    private val preferenceRepository: PreferenceRepository
    ) : ViewModel() {

    /**
     * TODO at first keep all state in one ViewModel; when it becomes not convenient, refactor code
     * to expose business or functional isolated pieces
     * */
    private val placeholder: Response<List<Trip>> = Response.Data(Collections.emptyList())

    private val _trips = MutableStateFlow(placeholder)
    val trips: StateFlow<Response<List<Trip>>> = _trips

    @Deprecated("use UI state instead")
    private val _isLoading = MutableStateFlow(true)
    @Deprecated("use UI state instead")
    val isLoading: StateFlow<Boolean> = _isLoading

    private val tripPlaceholder: Response<Trip> = Response.Data<Trip>(Trip())
    private val _trip = MutableStateFlow(tripPlaceholder)
    val trip: StateFlow<Response<Trip>> = _trip

    private val tripBookStatePlaceholder: Response<ServiceMessage> =
        Response.Data(ServiceMessage(ServiceMessage.Status.NONE))
    private val _tripBookState = MutableStateFlow(tripBookStatePlaceholder)
    val tripBookState: StateFlow<Response<ServiceMessage>> = _tripBookState

    private val _createTripState = MutableStateFlow(
        Response.Data(
            ServiceMessage(ServiceMessage.Status.NONE)) as Response<ServiceMessage>
    )
    val createTripState: StateFlow<Response<ServiceMessage>> = _createTripState

    private val driverPreferencesFlow = preferenceRepository.driverPreferencesFlow
    // TODO verify UI logic with _newDriverState
    @Deprecated("use UI state instead")
    private val _newDriverState = MutableStateFlow(Response.Loading(DriverCredentials()) as Response<DriverCredentials>)
    @Deprecated("use UI state instead")
    val newDriverState: StateFlow<Response<DriverCredentials>> = _newDriverState

    private val newCreateTripState = MutableStateFlow(CreateTripState())
    val newCreateTripUiState = newCreateTripState
        .map { it }
        .stateIn(viewModelScope, SharingStarted.Eagerly, CreateTripState())

    fun createTrip(trip: Trip) {
        viewModelScope.launch {
            repo.createTrip(trip)
                .stateIn(viewModelScope)
                .catch { ex ->
                    Log.e(App.TAG, "Something went wrong with creating the trip $trip", ex)
                    Response.Error.Exception(ex)
                }
                .collect { it ->
                    _createTripState.value = it
                }
        }
    }

    fun getTrips(locationFrom: String, locationTo: String, date: Long) {
        viewModelScope.launch {
            repo.getTrips(locationFrom, locationTo, date)
                .stateIn(viewModelScope)
                .onCompletion {
                    _isLoading.value = false
                }
                .catch { e ->
                    Log.e(App.TAG, "Something went wrong on loading trips for $locationFrom and ${Date(date)}", e)
                    Response.Error.Exception(e)
                }
                .collect { it ->
                    _trips.value = it
                }
        }
    }

    fun getTripById(id: String) {
        viewModelScope.launch {
            repo.getTripById(id)
                .stateIn(viewModelScope)
                .onStart {
                    _isLoading.value = true
                }
                .onCompletion { e ->
                    _isLoading.value = false
                }
                .catch { e ->
                    Log.e(App.TAG, "Something went wrong on loading with id $id", e)
                    Response.Error.Exception(e)
                }
                .collect { it ->
                    _trip.value = it
                }
        }
    }

    fun bookTrip(tripId: String) {
        viewModelScope.launch {
            repo.bookTrip(tripId)
                .stateIn(viewModelScope)
                .onStart {
                    _isLoading.value = true
                }
                .onCompletion { e ->
                    _isLoading.value = false
                }
                .catch { e ->
                    Log.e(App.TAG, "Something went wrong on loading with id $tripId", e)
                    Response.Error.Exception(e)
                }
                .collect { it ->
                    _tripBookState.value = it
                }
        }
    }

    fun createDriver(driverCredentials: DriverCredentials) {
        viewModelScope.launch {
            preferenceRepository.saveDriver(driverCredentials)
                    .transform<DriverCredentials, Response<ServiceMessage>> {
                        repo.createDriver(driverCredentials = driverCredentials)
                    }
                    .transform<Response<ServiceMessage>, Response<DriverCredentials>> {
                        repo.getDriver(cell = driverCredentials.cell, secret = driverCredentials.secret)
                    }
                    .collect { driverResponse ->
                        // TODO depends on response continue operation flow and refactor existing codebase
                        // TODO complete me, consider pros&cons of work with separate Error and Success states,
                        // also consider pros&cons dealing with different states on UI or ViewModel layers
//                        _newDriverState.value = it
                        when (driverResponse) {
                            is Response.Data -> {
                                newCreateTripState.update { state ->
                                    state.copy(driver = driverResponse.data)
                                }
                            }
                            is Response.Error.Message -> {
                                newCreateTripState.update { state ->
                                    val errors = state.errors + getErrorMessage(driverResponse)
                                    state.copy(errors = errors)
                                }
                            }
                            is Response.Error.Exception -> {
                                newCreateTripState.update { state ->
                                    val errors = state.errors + getErrorMessage(driverResponse)
                                    state.copy(errors = errors)
                                }
                            }
                        }
                    }
        }
    }

    fun getErrorMessage(errorResponse: Response.Error): String {
        var errorMessage = ""
        if (errorResponse is Response.Error.Exception) {
            Log.e(App.TAG, "Driver state is error", (errorResponse as Response.Error.Exception).error)
            errorMessage = context.resources.getString(R.string.default_error_message)
        } else {
            errorMessage = context.resources.getString(R.string.error_message_with_server_response, (errorResponse as Response.Error.Message).msg)
        }
        return errorMessage
    }

    fun getDriver() {
        viewModelScope.launch {
            Log.d(App.TAG, "New request for driver")
            driverPreferencesFlow
                    .stateIn(viewModelScope)
//                    .flatMapConcat { it ->
//                        repo.getDriver(cell = it.driver.cell, secret = it.driver.secret)
//                    }
/*                    .transform<DriverPreferences, Response<DriverCredentials>> { it ->
                        Log.d(App.TAG, "Got driver from cache, ask for driver from server")
                        repo.getDriver(it.driver.cell, it.driver.secret)
                    }*/
                    .onStart {
                        _isLoading.value = true

                    }
//                    .onCompletion { e ->
//                        _isLoading.value = false
//                    }
                    .catch { e ->
                        Log.e(App.TAG, "Something went wrong on loading with driver", e)
                        Response.Error.Exception(e)
                    }
                    .collect { it ->
                        Log.d(App.TAG, "Got driver from server")
                        Log.d(App.TAG, "Collect new driver state ${it.javaClass}")
                        _newDriverState.value = Response.Data(it.driver)
                    }
        }
    }

    fun errorShown(error: String) {
        newCreateTripState.update { it ->
            val errors = it.errors.filterNot { it.equals(error) }
            it.copy(errors = errors)
        }
    }

}

/*
data class CreateTripState(
    val driver: DriverCredentials = DriverCredentials(),
    val tripStatus: ServiceMessage = ServiceMessage(),
    val isLoading: Boolean = false,
    val errors: List<String> = emptyList()
)*/
