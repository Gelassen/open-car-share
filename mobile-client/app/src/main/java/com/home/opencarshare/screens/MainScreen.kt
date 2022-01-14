package com.home.opencarshare.screens

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.compose.rememberNavController
import com.home.opencarshare.AppNavGraph
import com.home.opencarshare.navigation.AppNavigation
import com.home.opencarshare.screens.viewmodel.DriverViewModel
import com.home.opencarshare.screens.viewmodel.TripsViewModel
import com.home.opencarshare.ui.theme.OpenCarShareTheme

@ExperimentalUnitApi
@Composable
fun MainScreen(driverViewModel: DriverViewModel, passengerViewModel: TripsViewModel) {
    val context = LocalContext.current
    val navController = rememberNavController()

    OpenCarShareTheme {
        Surface(color = MaterialTheme.colors.background) {
            AppNavGraph(
                context = context,
                driverViewModel = driverViewModel,
                passengerViewModel = passengerViewModel,
                navController = navController,
                startDestination = AppNavigation.Start.START
            )
        }
    }
}
