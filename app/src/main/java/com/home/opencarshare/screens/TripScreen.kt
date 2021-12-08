package com.home.opencarshare.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.home.opencarshare.App
import com.home.opencarshare.navigation.AppNavigation
import com.home.opencarshare.model.Trip
import com.home.opencarshare.network.Response
import com.home.opencarshare.screens.viewmodel.TripsViewModel

@Composable
fun TripScreen(viewModel: TripsViewModel, navController: NavController, searchTrip: Trip) {
    Log.d(App.TAG, "Trips: ${searchTrip.toString()}")

    val state by viewModel.trips.collectAsState()
    LaunchedEffect(viewModel) {
        viewModel.getTrips(
            searchTrip.locationFrom,
            searchTrip.locationTo,
            System.currentTimeMillis()
        )
    }
    TripsComposeList(state = state, navController = navController)
}

@Composable
fun TripsComposeList(
    state: Response<List<Trip>>,
    navController: NavController
) {
    val scrollState = rememberLazyListState()
    LazyColumn(
        state = scrollState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        when (state) {
            is Response.Data -> {
                items(state.data) { it ->
                    TripViewItem(
                        data = it,
                        { tripId ->
                            navController.navigate("${AppNavigation.Booking.TRIP_BOOKING}/$tripId")
                        },
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
            is Response.Error -> {
                // TODO show error (also consider case when you have to clear the list from previous data)
            }
        }
    }
}

