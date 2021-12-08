package com.home.opencarshare.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.home.opencarshare.App
import com.home.opencarshare.R
import com.home.opencarshare.model.Driver
import com.home.opencarshare.network.Response
import com.home.opencarshare.screens.elements.SingleCard
import com.home.opencarshare.screens.viewmodel.TripsViewModel
import java.lang.IllegalStateException

@Composable
fun TripBookingScreen(data: String?, viewModel: TripsViewModel, navController: NavController) {
    Log.d(App.TAG, "data:$data")

    LaunchedEffect(viewModel) {
        viewModel.getTripById(data!!)
    }

    SingleCard(content = {
        TripBookingContent(
            viewModel = viewModel,
            onBookingClick = { tripId -> /* TODO send request on the server and show confirmation page*/ }
        )
    })

}

@Composable
fun TripBookingContent(viewModel: TripsViewModel, onBookingClick: (String) -> Unit) {
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    val mainPadding = dimensionResource(id = R.dimen.main_margin_compact)
    var componentSpace = dimensionResource(id = R.dimen.component_space)
    val state by viewModel.trip.collectAsState()
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

        when (state) {
            is Response.Data -> {
                TripViewItem(
                    data = (state as Response.Data).data,
                    {},
                    modifier = Modifier.padding(vertical = baselineGrid)
                )
                Text(
                    text = "  Your driver  ",
                    color = colorResource(id = R.color.white),
                    modifier = Modifier
                        .height(componentSpace)
                        .padding(start = baselineGrid)
                        .fillMaxWidth()
                        .background(color = colorResource(id = R.color.design_default_color_secondary_variant))
                )

/*                Spacer(modifier = Modifier
                    .height(componentSpace)
                    .background(color = colorResource(id = R.color.design_default_color_secondary_variant)))*/
                DriverCard(data = (state as Response.Data).data.driver)
                Button(
                    onClick = { onBookingClick((state as Response.Data).data.id) },
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
                        text = stringResource(id = R.string.search_screen_confirm_button),
                        modifier = Modifier.align(Alignment.CenterVertically),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            is Response.Error -> {
                throw IllegalStateException("Not implemented yet")
            }
        }

    }
}

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
            text = "Passed trips: ${data.tripsCount}",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(textPadding),
            fontSize = textSize
        )
        Text(
            text = "Cell: ${data.cell}",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(textPadding),
            fontSize = textSize
        )
    }
}