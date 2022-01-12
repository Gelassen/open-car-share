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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.home.opencarshare.App
import com.home.opencarshare.R
import com.home.opencarshare.model.pojo.ServiceMessage
import com.home.opencarshare.model.pojo.Trip
import com.home.opencarshare.network.Response
import com.home.opencarshare.screens.elements.DriverCardContent
import com.home.opencarshare.screens.elements.ErrorPlaceholder
import com.home.opencarshare.screens.elements.SingleCard
import com.home.opencarshare.screens.viewmodel.PassengerTripUiState
import com.home.opencarshare.screens.viewmodel.TripsViewModel
import kotlinx.coroutines.launch

@Composable
fun TripBookingScreen(data: String?, viewModel: TripsViewModel, navController: NavController) {
    Log.d(App.TAG, "data:$data")

    LaunchedEffect(viewModel) {
        viewModel.getTripById(data!!)
    }

//    SingleCard(content = {
//        TripBookingContent(
//            state = state,
//            onBookingClick = { tripId -> /* no op */ }
//        )
//    })

}

@Composable
fun TripBookingContent(state: PassengerTripUiState.TripBookUiState) {
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
            data = state.trip,
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
        DriverCardContent(data = state.driver.toDriver())
        /**
         * Disable book button at current (alfa version), for more details
         * see {@link https://github.com/Gelassen/open-car-share/issues/3}
         * */
        Button(
            onClick = { /* no op */ },
            modifier = Modifier
                .alpha(0F)
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

/*@Composable
fun TripBookingContent(state: PassengerTripUiState/*viewModel: TripsViewModel*/, onBookingClick: (String) -> Unit) {
//    val tripState by viewModel.trip.collectAsState()
//    val bookingState by viewModel.tripBookState.collectAsState()

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
}*/

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
        DriverCardContent(data = (state as Response.Data).data.driver)
        /**
         * Disable book button at current (alfa version), for more details
         * see {@link https://github.com/Gelassen/open-car-share/issues/3}
         * */
        Button(
            onClick = { onBookingClick(state.data.id) },
            modifier = Modifier
                .alpha(0F)
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

