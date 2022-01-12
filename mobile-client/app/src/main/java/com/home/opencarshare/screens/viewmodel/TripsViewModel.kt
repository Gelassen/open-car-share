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
        val driver: DriverCredentials = DriverCredentials(),
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
    val driver: DriverCredentials = DriverCredentials(),
    val isLoading: Boolean,
    val errors: List<String>,
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

    private val driverPreferencesFlow = preferenceRepository.driverPreferencesFlow

    /*private val newCreateTripState = MutableStateFlow(CreateTripState())
    val newCreateTripUiState = newCreateTripState
        .map { it }
        .stateIn(viewModelScope, SharingStarted.Eagerly, CreateTripState())*/

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

/*    fun bookTrip(tripId: String) {
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
    }*/

}

/*
data class CreateTripState(
    val driver: DriverCredentials = DriverCredentials(),
    val tripStatus: ServiceMessage = ServiceMessage(),
    val isLoading: Boolean = false,
    val errors: List<String> = emptyList()
)*/
