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
import com.home.opencarshare.screens.TripBookingScreen
import com.home.opencarshare.screens.TripScreen
import com.home.opencarshare.screens.TripSearchScreen
import com.home.opencarshare.screens.viewmodel.TripsViewModel

@Composable
fun AppNavGraph(
    context: Context,
    viewModel: TripsViewModel,
    navController: NavHostController,
    startDestination: String
) {

    NavHost(navController = navController, startDestination = startDestination) {
        composable(
            AppNavigation.Search.TRIP_SEARCH
        ) {
            TripSearchScreen(onSearchClick = { locationFrom, locationTo, date ->
                // TODO validate data and navigate to the next screen
                Toast.makeText(context, "$locationFrom - $locationTo at $date", Toast.LENGTH_LONG).show()
                navController.navigate(
                    "${AppNavigation.Trips.TRIPS}" +
                            "?${AppNavigation.Trips.ARG_TRIP_LOCATION_FROM}=$locationFrom" +
                            "&${AppNavigation.Trips.ARG_TRIP_LOCATION_TO}=$locationTo" +
                            "&${AppNavigation.Trips.ARG_TRIP_DATE}=$date",
                )
            })
        }
        composable(
            route = "${AppNavigation.Trips.TRIPS}" +
                    "?${AppNavigation.Trips.ARG_TRIP_LOCATION_FROM}={locationFrom}" +
                    "&${AppNavigation.Trips.ARG_TRIP_LOCATION_TO}={locationTo}" +
                    "&${AppNavigation.Trips.ARG_TRIP_DATE}={date}",
            arguments = listOf(
                navArgument(AppNavigation.Trips.ARG_TRIP_LOCATION_FROM) { defaultValue = "" },
                navArgument(AppNavigation.Trips.ARG_TRIP_LOCATION_TO) { defaultValue = "" },
                navArgument(AppNavigation.Trips.ARG_TRIP_DATE) { defaultValue = "" }
            )
        ) { navBackStackEntry ->
            val searchTrip = ArgsToTripConverter().convertArgsToTrip(navBackStackEntry)
            TripScreen(searchTrip = searchTrip, viewModel = viewModel, navController = navController)
        }
        composable(
            "${AppNavigation.Booking.TRIP_BOOKING}/{tripId}",
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val tripId = navBackStackEntry.arguments?.getString(AppNavigation.ARG_TRIP_ID)
            TripBookingScreen(
                data = tripId,
                viewModel = viewModel,
                navController = navController) }
    }
}