package com.home.opencarshare.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.home.opencarshare.App
import com.home.opencarshare.R
import com.home.opencarshare.model.pojo.Driver
import com.home.opencarshare.model.pojo.DriverCredentials
import com.home.opencarshare.model.pojo.ServiceMessage
import com.home.opencarshare.model.pojo.Trip
import com.home.opencarshare.screens.elements.*
import com.home.opencarshare.screens.viewmodel.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * NOTE
 * TripCreateScreen trigger navigateToDriver when ServiceMessage status has been updated,
 * each time when state has been updated previous ServiceMessage status will trigger navigateToDriver
 * recursively. DisposableEffect() seems wouldn't fit this scenario, another option
 * is to add extra state to ServiceMessage for opening driver screen (which means it should
 * be converted to UI state), alternatively just pop up current screen from stack
 * */
@Composable
fun CreateTripContent(
    viewModel: DriverViewModel,
    state: CreateTripUiState.NewTripUiState,
    onCreateClick: (Trip) -> Unit
) {
    if (state.errors.isEmpty()) {
        when(state.tripStatus) {
            TripStatus.NONE -> {
                SingleCard(content = {
                    TripCreateScreenContent(
                        driver = state.driver,
                        onCreateClick = { locationFromTxt, locationToTxt, pickUpDate ->
                            onCreateClick(
                                Trip(
                                    locationFrom = locationFromTxt,
                                    locationTo = locationToTxt,
                                    date = pickUpDate,
                                    driverId = state.driver.id
                                )
                            )
                        }
                    )
                })
            }
            TripStatus.SUCCEED_WAIT_FOR_ACTION -> {
                viewModel.onAfterTripCreateAction()
            }
            TripStatus.SUCCEED -> {
                // no op
                Log.d(App.TAG, "[status] create::trip - navigate action is done")
            }
            TripStatus.FAILED -> {
                if (!state.errors.isEmpty()) {
                    val errorMessage = stringResource(id = R.string.error_message_with_server_response, state.errors.last())
                    showError(LocalContext.current, errorMessage)
                    viewModel.errorShown(errorMessage)
                }
                SingleCard(content = {
                    TripCreateScreenContent(
                        driver = state.driver,
                        onCreateClick = { locationFromTxt, locationToTxt, pickUpDate ->
                            onCreateClick(Trip(locationFrom = locationFromTxt, locationTo = locationToTxt, date = pickUpDate))
                        }
                    )
                })
            }
        }
    } else {
        val errorMessage = state.errors.last()
        showError(LocalContext.current, errorMessage)
        viewModel.errorShown(errorMessage)
    }
}

@Composable
fun TripCreateScreenContent(
    driver: DriverCredentials,
    onCreateClick: (locationFrom: String, locationTo: String, date: String) -> Unit) {
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    var componentSpace = dimensionResource(id = R.dimen.component_space)
    var locationFromTxt by remember { mutableStateOf("") }
    var locationToTxt by remember { mutableStateOf("") }
    var pickUpDate by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
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
        Text(
            text = stringResource(id = R.string.booking_screen_driver),
            color = colorResource(id = R.color.white),
            modifier = Modifier
                .height(componentSpace)
                .padding(start = baselineGrid)
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.design_default_color_secondary_variant))
        )
        DriverCardContent(
                data = Driver(
                    id = driver.id,
                    name = driver.name,
                    cell = driver.cell,
                    tripsCount = driver.tripsCount)
        )
        Button(
            onClick = { onCreateClick(locationFromTxt, locationToTxt, pickUpDate) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(baselineGrid)
                .height(dimensionResource(id = R.dimen.button_height)),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue, contentColor = Color.White),
        ) {
            Text(
                text = stringResource(id = R.string.create_trip_screen_confirm_button),
                modifier = Modifier.align(Alignment.CenterVertically),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun showError(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}