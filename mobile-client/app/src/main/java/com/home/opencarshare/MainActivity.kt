package com.home.opencarshare

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.home.opencarshare.screens.MainScreen
import com.home.opencarshare.screens.viewmodel.DriverViewModel
import com.home.opencarshare.screens.viewmodel.TripsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val driverViewModel: DriverViewModel by viewModels()
    private val passengerViewModel: TripsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())

        setContent {
            MainScreen(driverViewModel = driverViewModel, passengerViewModel = passengerViewModel)
        }
    }

}

