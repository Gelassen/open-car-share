package com.home.opencarshare.screens

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.home.opencarshare.R
import com.home.opencarshare.screens.elements.DriverCardContent
import com.home.opencarshare.screens.viewmodel.PassengerTripUiState

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