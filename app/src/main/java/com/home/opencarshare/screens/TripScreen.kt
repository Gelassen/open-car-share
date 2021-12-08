package com.home.opencarshare.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.home.opencarshare.App
import com.home.opencarshare.R
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
    TripsComposeList(
        state = state,
        onClick = { tripId -> navController.navigate("${AppNavigation.Booking.TRIP_BOOKING}/$tripId")},
    )
}

@Composable
fun TripsComposeList(
    state: Response<List<Trip>>,
    onClick: (String) -> Unit,
) {
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    val mainPadding = dimensionResource(id = R.dimen.main_margin_compact)
    val componentSpace = dimensionResource(id = R.dimen.component_space)
    val elevation = dimensionResource(id = R.dimen.elevation)
    val scrollState = rememberLazyListState()
    LazyColumn(
        state = scrollState,
        contentPadding = PaddingValues(horizontal = mainPadding, vertical = baselineGrid),
        modifier = Modifier
            .fillMaxHeight()
            .background(color = colorResource(id = R.color.colorPrimaryDark))
    ) {
        when (state) {
            is Response.Data -> {
                items(state.data) { it ->
                    TripViewItem(
                        data = it,
                        onClick = { tripId -> onClick(tripId) },
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .background(colorResource(id = R.color.white))
                    )
                }
            }
            is Response.Error -> {
                // TODO show error (also consider case when you have to clear the list from previous data)
            }
        }
    }
}

