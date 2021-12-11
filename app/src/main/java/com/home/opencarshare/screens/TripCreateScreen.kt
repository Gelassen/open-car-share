package com.home.opencarshare.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.home.opencarshare.R
import com.home.opencarshare.model.pojo.Driver
import com.home.opencarshare.model.pojo.ServiceMessage
import com.home.opencarshare.model.pojo.Trip
import com.home.opencarshare.network.Response
import com.home.opencarshare.screens.elements.DriverCardContent
import com.home.opencarshare.screens.elements.DriverCardContentEditable
import com.home.opencarshare.screens.elements.SingleCard
import com.home.opencarshare.screens.elements.TextFieldEditable
import com.home.opencarshare.screens.viewmodel.TripsViewModel
import kotlinx.coroutines.launch


@Composable
fun TripCreateScreen(viewModel: TripsViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val uiState = viewModel.createTripUiModel.collectAsState()
    LaunchedEffect(viewModel) {
        viewModel.getDriver()
    }
    // TODO support back navigation
    // TODO show drive profile on trip
    // TODO response on CreateTrip status
    if (uiState.value.state is Response.Error) {
        Toast.makeText(LocalContext.current, stringResource(id = R.string.create_trip_error_msg), Toast.LENGTH_SHORT).show()
    } else if (uiState.value.state is Response.Data) {
        when((uiState.value.state as Response.Data).data.status) {
            ServiceMessage.Status.NONE -> {
                if (uiState.value.driver.isEmpty()) {
                    SingleCard(content = {
                        DriverCardContentEditable(
                            onConfirmClick = { driver ->
                                coroutineScope.launch {
                                    viewModel.saveDriver(driver = driver)
                                }
                            }
                        )
                    })
                } else {
                    SingleCard(content = {
                        TripCreateScreenContent(
                            driver = uiState.value.driver,
                            onCreateClick = { locationFromTxt, locationToTxt, pickUpDate ->
                                coroutineScope.launch {
                                    viewModel.createTrip(
                                        Trip(locationFrom = locationFromTxt, locationTo = locationToTxt, date = pickUpDate)
                                    )
                                }
                            }
                        )
                    })
                }
            }
            ServiceMessage.Status.SUCCEED -> {
                // TODO navigate further:
            }
            ServiceMessage.Status.FAILED -> {

                if (uiState.value.driver.isEmpty()) {
                    SingleCard(content = {
                        DriverCardContentEditable(
                            onConfirmClick = { driver ->
                                coroutineScope.launch {
                                    viewModel.saveDriver(driver = driver)
                                }
                            }
                        )
                    })
                } else {
                    SingleCard(content = {
                        TripCreateScreenContent(
                            driver = uiState.value.driver,
                            onCreateClick = { locationFromTxt, locationToTxt, pickUpDate ->
                                coroutineScope.launch {
                                    viewModel.createTrip(
                                        Trip(locationFrom = locationFromTxt, locationTo = locationToTxt, date = pickUpDate)
                                    )
                                }
                            }
                        )
                    })
                }
            }
        }
    }
}

@Composable
fun TripCreateScreenContent(
    driver: Driver,
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
        DriverCardContent(data = driver)
        Button(
            onClick = { onCreateClick(locationFromTxt, locationToTxt, pickUpDate) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(baselineGrid)
                .height(dimensionResource(id = R.dimen.button_height)),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue, contentColor = Color.White),
        ) {
            Text(
                text = stringResource(id = R.string.search_screen_confirm_button),
                modifier = Modifier.align(Alignment.CenterVertically),
                fontWeight = FontWeight.Bold
            )
        }
    }
}