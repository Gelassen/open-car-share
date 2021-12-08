package com.home.opencarshare.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.home.opencarshare.App
import com.home.opencarshare.network.Response
import com.home.opencarshare.screens.viewmodel.TripsViewModel
import java.lang.IllegalStateException

@Composable
fun TripBookingScreen(data: String?, viewModel: TripsViewModel, navController: NavController) {
    Log.d(App.TAG, "data:$data")
    val state by viewModel.trip.collectAsState()
    LaunchedEffect(viewModel) {
        viewModel.getTripById(data!!)
    }
    Box(
        modifier = Modifier.fillMaxHeight()
            .fillMaxWidth()
            .padding(paddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp))
    ) {

        when (state) {
            is Response.Data -> {
                TripViewItem(
                    data = (state as Response.Data).data,
                    {},
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
            is Response.Error -> {
                throw IllegalStateException("Not implemented yet")
            }
        }

    }
}