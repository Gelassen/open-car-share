package com.home.opencarshare.screens.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.home.opencarshare.App
import com.home.opencarshare.model.CreateTripUiModel
import com.home.opencarshare.model.pojo.ServiceMessage
import com.home.opencarshare.model.pojo.Trip
import com.home.opencarshare.network.Repository
import com.home.opencarshare.network.Response
import com.home.opencarshare.persistent.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TripsViewModel
@Inject constructor(
    private val repo: Repository,
    private val preferenceRepository: PreferenceRepository
    ) : ViewModel() {

    private val placeholder: Response<List<Trip>> = Response.Data(Collections.emptyList())

    private val _trips = MutableStateFlow(placeholder)
    val trips: StateFlow<Response<List<Trip>>> = _trips

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val tripPlaceholder: Response<Trip> = Response.Data<Trip>(Trip())
    private val _trip = MutableStateFlow(tripPlaceholder)
    val trip: StateFlow<Response<Trip>> = _trip

    private val tripBookStatePlaceholder: Response<ServiceMessage> =
        Response.Data(ServiceMessage(ServiceMessage.Status.NONE))
    private val _tripBookState = MutableStateFlow(tripBookStatePlaceholder)
    val tripBookState: StateFlow<Response<ServiceMessage>> = _tripBookState

    private val driverPreferencesFlow = preferenceRepository.driverPreferencesFlow
/*    private val tripCreateStatePlaceholder: Response<ServiceMessage> =
        Response.Data(ServiceMessage(ServiceMessage.Status.NONE))
    private val _tripCreateState = MutableStateFlow(tripCreateStatePlaceholder)
    val tripCreateState: StateFlow<Response<ServiceMessage>> = _tripCreateState*/
    private val _createTripUiModel = MutableStateFlow(CreateTripUiModel())
    val createTripUiModel: StateFlow<CreateTripUiModel> = _createTripUiModel

    fun getDriver() {
        viewModelScope.launch {
            driverPreferencesFlow
                .stateIn(viewModelScope)
                .collect { it ->
                    _createTripUiModel.value.driver.from(it.driver)
                }
        }
    }

    fun createTrip(trip: Trip) {
        viewModelScope.launch {
            repo.createTrip(trip)
                .stateIn(viewModelScope)
                .catch { ex ->
                    Log.e(App.TAG, "Something went wrong with creating the trip $trip", ex)
                    Response.Error.Exception(ex)
                }
                .collect { it ->
                    _createTripUiModel.value.state = it
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
}