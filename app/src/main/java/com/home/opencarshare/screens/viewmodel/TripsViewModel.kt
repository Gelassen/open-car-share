package com.home.opencarshare.screens.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.home.opencarshare.App
import com.home.opencarshare.model.Trip
import com.home.opencarshare.network.Repository
import com.home.opencarshare.network.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TripsViewModel @Inject constructor(val repo: Repository) : ViewModel() {

    private val placeholder: Response<List<Trip>> = Response.Data(Collections.emptyList())

    private val _trips = MutableStateFlow(placeholder)
    val trips: StateFlow<Response<List<Trip>>> = _trips

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val tripPlaceholder: Response<Trip> = Response.Data<Trip>(Trip())
    private val _trip = MutableStateFlow(tripPlaceholder)
    val trip: StateFlow<Response<Trip>> = _trip

    val searchTrip/*: StateFlow<Trip>*/ = MutableStateFlow(Trip())

//    val text: MutableState<String> = mutableStateOf("a")
    val tmp: Trip = Trip()

    fun getTrips(city: String, date: Long) {
        viewModelScope.launch {
            repo.getTrips(city, date)
                .stateIn(viewModelScope)
                .onCompletion {
                    _isLoading.value = false
                }
                .catch { e ->
                    Log.e(App.TAG, "Something went wrong on loading trips for $city and ${Date(date)}", e)
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
                .onCompletion { e ->
                    _isLoading.value = false
                }
                .catch { e ->
                    Log.e(App.TAG, "Something went wrong on loading with id $id", e)
                }
                .collect { it ->
                    _trip.value = it
                }
        }
    }
}