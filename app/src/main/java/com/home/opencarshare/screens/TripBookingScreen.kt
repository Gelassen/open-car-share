package com.home.opencarshare.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.home.opencarshare.di.Stubs
import com.home.opencarshare.ui.theme.OpenCarShareTheme

@Composable
fun TripBookingScreen(navController: NavController) {
    OpenCarShareTheme {
        Surface(color = MaterialTheme.colors.background) {
            TripViewItem(
                data = Stubs.Trips.generateTrip(),
                navController = navController,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}