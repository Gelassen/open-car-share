package com.home.opencarshare.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.home.opencarshare.App
import com.home.opencarshare.R
import com.home.opencarshare.model.pojo.Driver
import com.home.opencarshare.screens.elements.DriverCardContent
import com.home.opencarshare.screens.elements.ErrorCard
import com.home.opencarshare.screens.elements.SingleCard
import com.home.opencarshare.screens.viewmodel.CreateTripUiState
import com.home.opencarshare.screens.viewmodel.DriverViewModel

@Composable
fun DriverScreen(viewModel: DriverViewModel) {
    Log.d(App.TAG, "[screen] DriverScreen")
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(viewModel) {
        Log.d(App.TAG, "[DriverScreen] LaunchedEffect(viewModel) is called")
        viewModel.getDriver()
    }
    LaunchedEffect(viewModel) {
        Log.d(App.TAG, "[DriverScreen] LaunchedEffect(viewModel) for tripsByDriver() is called")
        viewModel.getTripsByDriver()
    }
    Log.d(App.TAG, "[screen] new state $state")
    if (state.errors.isEmpty()) {
        Log.d(App.TAG, "[state] errors is empty")
        when (state) {
            is CreateTripUiState.DriverHasTripsUiState -> {
                Log.d(App.TAG, "[state] DriverScreen::DriverHasTripsUiState")
                val driverState = state as CreateTripUiState.DriverHasTripsUiState
                SingleCard(
                    content = { DriverWithTripsContent(state = driverState, viewModel = viewModel) }
                )
            }
            is CreateTripUiState.NewTripUiState -> {
                Log.d(App.TAG, "[state] NewTripUiState")
            }
            else -> {
                Log.d(App.TAG, "[state] DriverScreen::otherState $state")
            }
        }

    } else {
        Log.d(App.TAG, "[state] ErrorCard")
        ErrorCard(state = state.errors.last(), viewModel = viewModel)
    }
}

@Composable
fun DriverWithTripsContent(state: CreateTripUiState.DriverHasTripsUiState, viewModel: DriverViewModel) {
    Box {
        Column {
            DriverCardContent(
                data = Driver(
                    name = state.driver.name,
                    cell = state.driver.cell,
                    tripsCount = state.driver.tripsCount
                )
            )

            TripsComposeList(
                state = state.tripsByDriver,
                onClick = { tripId -> viewModel.addTripInCancelList(tripId) },
                modifier = Modifier
                    .fillMaxHeight()
                    .background(color = colorResource(id = R.color.white))
            )
        }
        if (state.queueToCancel.isNotEmpty()) {
            ConfirmationDialog(
                tripId = state.queueToCancel.first(),
                onConfirmClick = { tripId -> viewModel.cancelTrip(tripId, state.driver)},
                onCancelClick = { tripId -> viewModel.removeTripFromCancelList(tripId)}
            )
        }
    }

}

@Composable
fun ConfirmationDialog(tripId: String, onConfirmClick: (String) -> Unit, onCancelClick: (String) -> Unit) {
    val openDialog = remember { mutableStateOf(true) }
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            text = {
                   Text(
                       text = stringResource(id = R.string.driver_screen_confirmation_message),
                       textAlign = TextAlign.Center
                   )
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .width(384.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.padding(baselineGrid),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Blue,
                            contentColor = Color.White
                        ),
                        onClick = { onConfirmClick(tripId) }) {
                        Text(
                            text = stringResource(id = R.string.driver_screen_button_confirm),
                            modifier = Modifier.align(Alignment.CenterVertically),
                            fontWeight = FontWeight.Bold)
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Blue,
                            contentColor = Color.White
                        ),
                        onClick = { onCancelClick(tripId) }) {
                        Text(
                            text = stringResource(id = R.string.driver_screen_button_cancel),
                            modifier = Modifier.align(Alignment.CenterVertically),
                            fontWeight = FontWeight.Bold)
                    }
                }
            },
            properties = DialogProperties(dismissOnClickOutside = false)
        )
    }
}
