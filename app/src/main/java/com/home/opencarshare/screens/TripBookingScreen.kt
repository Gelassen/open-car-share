package com.home.opencarshare.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.home.opencarshare.App
import com.home.opencarshare.R
import com.home.opencarshare.model.pojo.Driver
import com.home.opencarshare.model.pojo.ServiceMessage
import com.home.opencarshare.model.pojo.Trip
import com.home.opencarshare.network.Response
import com.home.opencarshare.screens.elements.ErrorPlaceholder
import com.home.opencarshare.screens.elements.SingleCard
import com.home.opencarshare.screens.viewmodel.TripsViewModel
import kotlinx.coroutines.launch

@Composable
fun TripBookingScreen(data: String?, viewModel: TripsViewModel, navController: NavController) {
    Log.d(App.TAG, "data:$data")

    LaunchedEffect(viewModel) {
        viewModel.getTripById(data!!)
    }

    val scope = rememberCoroutineScope()

    SingleCard(content = {
        TripBookingContent(
            viewModel = viewModel,
            onBookingClick = { tripId ->
            /* TODO send request on the server and show confirmation page*/
                scope.launch {
                    viewModel.bookTrip(tripId)
                }
            }
        )
    })

}

@Composable
fun TripBookingContent(viewModel: TripsViewModel, onBookingClick: (String) -> Unit) {
    val tripState by viewModel.trip.collectAsState()
    val bookingState by viewModel.tripBookState.collectAsState()

    if (tripState is Response.Error) {
        TripBookingErrorState(state = (tripState as Response.Error))
    } else if (tripState is Response.Data && bookingState is Response.Error) {
        TripBookingInitialState(state = tripState as Response.Data<Trip>, onBookingClick = onBookingClick)
    } else if (tripState is Response.Data && bookingState is Response.Data) {
        TripBookingInitialState(state = tripState as Response.Data<Trip>, onBookingClick = onBookingClick)
        val bookingTripStatus = (bookingState as Response.Data).data
        when (bookingTripStatus.status) {
            ServiceMessage.Status.NONE -> {
                // no op
            }
            ServiceMessage.Status.SUCCEED -> {
                Toast.makeText(LocalContext.current, "Now you have to call the driver to confirm all details", Toast.LENGTH_LONG).show()
            }
            ServiceMessage.Status.FAILED -> {
                Toast.makeText(LocalContext.current, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Composable
fun TripBookingInitialState(state: Response.Data<Trip>, onBookingClick: (String) -> Unit) {
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    val mainPadding = dimensionResource(id = R.dimen.main_margin_compact)
    var componentSpace = dimensionResource(id = R.dimen.component_space)
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(
                paddingValues = PaddingValues(
                    horizontal = mainPadding,
                    vertical = baselineGrid
                )
            )
    ) {
        TripViewItem(
            data = state.data,
            {},
            modifier = Modifier.padding(vertical = baselineGrid)
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
        DriverCard(data = (state as Response.Data).data.driver)
        Button(
            onClick = { onBookingClick(state.data.id) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(baselineGrid)
                .height(dimensionResource(id = R.dimen.button_height)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Blue,
                contentColor = Color.White
            ),
        ) {
            Text(
                text = stringResource(id = R.string.booking_screen_confirm_button),
                modifier = Modifier.align(Alignment.CenterVertically),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TripBookingErrorState(state: Response.Error) {
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    val mainPadding = dimensionResource(id = R.dimen.main_margin_compact)
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(
                paddingValues = PaddingValues(
                    horizontal = mainPadding,
                    vertical = baselineGrid
                )
            )
    ) {
        lateinit var errorText: String
        if (state is Response.Error.Message) {
            errorText = stringResource(id = R.string.error_message_with_server_response, state.msg)
        } else {
            errorText = stringResource(id = R.string.default_error_message)
        }
        ErrorPlaceholder(text = errorText)
    }
}

/*@Composable
fun TripBookingInnerContent(state: Response.Data<Trip>, onBookingClick: (String) -> Unit) {
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    val mainPadding = dimensionResource(id = R.dimen.main_margin_compact)
    var componentSpace = dimensionResource(id = R.dimen.component_space)
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(
                paddingValues = PaddingValues(
                    horizontal = mainPadding,
                    vertical = baselineGrid
                )
            )
    ) {
        TripViewItem(
            data = state.data,
            {},
            modifier = Modifier.padding(vertical = baselineGrid)
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
        DriverCard(data = (state as Response.Data).data.driver)
        Button(
            onClick = { onBookingClick(state.data.id) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(baselineGrid)
                .height(dimensionResource(id = R.dimen.button_height)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Blue,
                contentColor = Color.White
            ),
        ) {
            Text(
                text = stringResource(id = R.string.booking_screen_confirm_button),
                modifier = Modifier.align(Alignment.CenterVertically),
                fontWeight = FontWeight.Bold
            )
        }
    }
}*/

@Composable
fun DriverCard(data: Driver) {
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    val mainPadding = dimensionResource(id = R.dimen.main_margin_compact)
    val textSize = 18.sp//dimensionResource(id = R.dimen.text_size)
    val textPadding = dimensionResource(id = R.dimen.text_padding)
    Column(modifier = Modifier
        .padding(
            top = baselineGrid,
            bottom = baselineGrid,
            start = mainPadding,
            end = mainPadding
        )
        .fillMaxWidth()) {
        Text(
            text = data.name,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(textPadding),
            fontSize = textSize
        )
        Text(
            text = stringResource(id = R.string.booking_screen_passed_trips, data.tripsCount),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(textPadding),
            fontSize = textSize
        )
        Text(
            text = stringResource(id = R.string.booking_screen_cell, data.cell),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(textPadding),
            fontSize = textSize
        )
    }
}