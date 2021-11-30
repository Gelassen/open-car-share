package com.home.opencarshare.screens

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
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

    fun getTrips(city: String, date: Long) {
        viewModelScope.launch {
            repo.getTrips(city, date)
                .stateIn(viewModelScope)
                .onCompletion {
                    _isLoading.value = false
                }
                .catch { e ->
                    Log.e(App.TAG, "Something went wrong on loading deputies", e)
                }
                .collect { it ->
                    _trips.value = it
                }

        }
    }
}