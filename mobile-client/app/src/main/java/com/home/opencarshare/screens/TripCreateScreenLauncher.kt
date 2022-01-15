package com.home.opencarshare.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.home.opencarshare.App
import com.home.opencarshare.screens.elements.DriverCardContentEditable
import com.home.opencarshare.screens.elements.ErrorCard
import com.home.opencarshare.screens.elements.SingleCard
import com.home.opencarshare.screens.viewmodel.CreateTripUiState
import com.home.opencarshare.screens.viewmodel.DriverViewModel
import kotlinx.coroutines.launch
import java.lang.RuntimeException

@Composable
fun TripCreateScreenLauncher(viewModel: DriverViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.uiState.collectAsState()
    val isForceRefresh = viewModel.forceRefresh.collectAsState()
    LaunchedEffect(viewModel) {
        viewModel.getDriver()
    }
    if (isForceRefresh.value) {
        Text(text = "isForceRefresh is called")
    }
    if (!state.errors.isEmpty()) {
        ErrorCard(state = state.errors.last(), viewModel = viewModel)
    } else {
        if (state.isLoading) {
            // for now just track it
            Log.d(App.TAG, "[TripCreateScreen] isLoading state on")
        }
        when (state) {
            is CreateTripUiState.NoDriverUiState -> {
                SingleCard(
                    content = {
                        DriverCardContentEditable(
                            onConfirmClick = { driverCredentials ->
                                coroutineScope.launch {
                                    viewModel.createDriver(driverCredentials)
                                }
                            }
                        )
                    }
                )
            }
            is CreateTripUiState.NewTripUiState -> {
                CreateTripContent(
                    viewModel = viewModel,
                    state = state as CreateTripUiState.NewTripUiState,
                    onCreateClick = { trip ->
                        coroutineScope.launch {
                            viewModel.createTrip(trip)
                        }
                    }
                )
            }
            is CreateTripUiState.DriverHasTripsUiState -> {
                /**
                 * Opening DriverScreen over navigation graph cause either recursive call or
                 * empty screen when new state has been introduced to mitigate the issue.
                 * */
                DriverScreen(viewModel = viewModel)
            }
        }
    }
}