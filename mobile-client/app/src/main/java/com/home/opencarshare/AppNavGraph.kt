package com.home.opencarshare

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.home.opencarshare.converters.ArgsToTripConverter
import com.home.opencarshare.navigation.AppNavigation
import com.home.opencarshare.screens.*
import com.home.opencarshare.screens.viewmodel.DriverViewModel
import com.home.opencarshare.screens.viewmodel.TripsViewModel

@Composable
fun AppNavGraph(
    context: Context,
    driverViewModel: DriverViewModel,
    passengerViewModel: TripsViewModel,
    navController: NavHostController,
    startDestination: String
) {

    NavHost(navController = navController, startDestination = startDestination) {
        composable(AppNavigation.Start.START) {
            StartScreen(
                onDriverClick = { navController.navigate(AppNavigation.Create.CREATE_LAUNCHER) },
                onPassengerClick = { navController.navigate(AppNavigation.Search.TRIP_SEARCH) }
            )
        }
        composable(AppNavigation.Create.CREATE_LAUNCHER) {
            TripCreateScreenLauncher(viewModel = driverViewModel)
        }
        composable(AppNavigation.Driver.DRIVER) {
            DriverScreen(viewModel = driverViewModel)
        }
        composable(AppNavigation.Search.TRIP_SEARCH) {
            TripSearchScreen(
                viewModel = passengerViewModel
            )
        }
    }
}