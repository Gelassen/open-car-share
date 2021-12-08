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
import com.home.opencarshare.converters.ArgsToTripConverter
import com.home.opencarshare.navigation.AppNavigation
import com.home.opencarshare.screens.MainScreen
import com.home.opencarshare.screens.TripBookingScreen
import com.home.opencarshare.screens.TripScreen
import com.home.opencarshare.screens.TripSearchScreen
import com.home.opencarshare.screens.viewmodel.TripsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: TripsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())

        setContent {
            MainScreen(viewModel = viewModel)
        }
    }
}

