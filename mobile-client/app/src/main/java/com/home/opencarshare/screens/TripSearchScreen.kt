package com.home.opencarshare.screens


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.home.opencarshare.App
import com.home.opencarshare.R
import com.home.opencarshare.providers.TripsProvider
import com.home.opencarshare.screens.elements.ErrorCard
import com.home.opencarshare.screens.elements.SingleCard
import com.home.opencarshare.screens.elements.TextFieldEditable
import com.home.opencarshare.screens.viewmodel.PassengerTripUiState
import com.home.opencarshare.screens.viewmodel.TripsViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun TripSearchScreen(
    viewModel: TripsViewModel,
    onSearchClick: (locationFrom: String, locationTo: String, date: Long) -> Unit,
    onTripsListUiState: (tripId: String) -> Unit,
    onTripsBookUiState: () -> Unit
) {
    Log.d(App.TAG, "[screen] TripSearchScreen")
    val scope = rememberCoroutineScope()
    val state by viewModel.uiState.collectAsState()
    if (state.errors.isNotEmpty()) {
        ErrorCard(state = state.errors.last())
    } else {
        if (state.isLoading) {
            Log.d(App.TAG, "[TripSearchScreen] isLoading state on")
        }
        when(state) {
            is PassengerTripUiState.SearchTripUiState -> {
                Log.d(App.TAG, "[state] SearchTripUiState")
                SingleCard(content = {
                    TripSearchContent(
                        onSearchClick = { locationFrom, locationTo, date ->
                            scope.launch {
                                viewModel.getTrips(
                                    locationFrom = locationFrom,
                                    locationTo = locationTo,
                                    date = date
                                )
                            }
                        })
                })
            }
            is PassengerTripUiState.TripsListUiState -> {
                Log.d(App.TAG, "[state] TripsListUiState")
                NewTripsComposeList(
                    state = (state as PassengerTripUiState.TripsListUiState).trips,
                    onClick = { tripId ->
                        scope.launch {
                            viewModel.getTripById(tripId)
                        }
                    },
                    modifier = Modifier)
            }
            is PassengerTripUiState.TripBookUiState -> {
                Log.d(App.TAG, "[state] TripBookUiState")
                SingleCard(content = {
                    TripBookingContent(state = state as PassengerTripUiState.TripBookUiState)
                })
            }
        }
    }

}

@Composable
fun TripSearchContent(onSearchClick: (locationFrom: String, locationTo: String, date: Long) -> Unit) {
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    val mainPadding = dimensionResource(id = R.dimen.main_margin_compact)
    // TODO figure out is there existing composable for date picker and numeric picker
    var locationFromTxt by remember { mutableStateOf("") }
    var locationToTxt by remember { mutableStateOf("") }
    var pickUpDate by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(mainPadding)) {
        TextFieldEditable(
            state = locationFromTxt,
            onTextChanged = { it -> locationFromTxt = it },
            hint = stringResource(id = R.string.search_screen_location_to_hint),
            icon = Icons.Default.LocationOn
        )
        TextFieldEditable(
            state = locationToTxt,
            onTextChanged = { it -> locationToTxt = it },
            hint = stringResource(id = R.string.search_screen_location_from_hint),
            icon = Icons.Default.LocationOn
        )
        TextFieldEditable(
            state = pickUpDate,
            onTextChanged = { it -> pickUpDate = it },
            hint = stringResource(id = R.string.search_screen_date),
            icon = null
        )
        Button(
            onClick = {
                onSearchClick(locationFromTxt, locationToTxt, TripsProvider().dateTimeAsLong(pickUpDate))
                Log.d(App.TAG, "On search click ${TripsProvider().dateTimeAsLong(pickUpDate)}")
                      },
            modifier = Modifier
                .fillMaxWidth()
                .padding(baselineGrid)
                .height(dimensionResource(id = R.dimen.button_height)),
            colors = buttonColors(backgroundColor = Color.Blue, contentColor = Color.White),
        ) {
            Text(
                text = stringResource(id = R.string.search_screen_confirm_button),
                modifier = Modifier.align(CenterVertically),
                fontWeight = FontWeight.Bold
            )
        }
    }
}