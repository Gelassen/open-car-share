package com.home.opencarshare.screens

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
import com.home.opencarshare.ui.theme.OpenCarShareTheme
import java.lang.IllegalStateException

@Composable
fun TripBookingScreen(data: String?, viewModel: TripsViewModel, navController: NavController) {
    Log.d(App.TAG, "data:$data")
    val state by viewModel.trip.collectAsState()
    LaunchedEffect(viewModel) {
        viewModel.getTripById(data!!)
    }
    OpenCarShareTheme {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxHeight()
                .fillMaxWidth()
                .padding(paddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp))
        ) {

            // TODO looks dirty, consider to move the switcher to another layer
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
}