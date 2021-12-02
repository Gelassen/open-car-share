package com.home.opencarshare

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.home.opencarshare.di.Stubs
import com.home.opencarshare.model.Trip
import com.home.opencarshare.navigation.AppNavigation
import com.home.opencarshare.screens.TripBookingScreen
import com.home.opencarshare.screens.TripScreen
import com.home.opencarshare.screens.TripSearchScreen
import com.home.opencarshare.screens.viewmodel.TripsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: TripsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())

        setContent {
            val context = LocalContext.current
            val navController = rememberNavController()
            TripScreen(viewModel = viewModel, navController = navController)
            NavHost(navController = navController, startDestination = AppNavigation.TRIP_SEARCH) {
                composable(AppNavigation.TRIP_SEARCH) {
                    TripSearchScreen(onSearchClick = { locationFrom, locationTo, date ->
                        // TODO validate data and navigate to the next screen
                        Toast.makeText(context, "$locationFrom - $locationTo at $date", Toast.LENGTH_LONG).show()
                    })
                }
                composable(AppNavigation.TRIPS) { TripScreen(viewModel = viewModel, navController = navController) }
                composable(
                    "${AppNavigation.TRIP_BOOKING}/{tripId}",
                    arguments = listOf(navArgument("tripId") { type = NavType.StringType })
                ) { navBackStackEntry ->
                    val tripId = navBackStackEntry.arguments?.getString(AppNavigation.ARG_TRIP_ID)
                    TripBookingScreen(
                        data = tripId,
                        viewModel = viewModel,
                        navController = navController) }
            }
        }
    }
}

