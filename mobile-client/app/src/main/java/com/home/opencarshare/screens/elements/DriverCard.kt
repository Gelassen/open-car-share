package com.home.opencarshare.screens.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.home.opencarshare.R
import com.home.opencarshare.model.pojo.Driver
import com.home.opencarshare.model.pojo.DriverCredentials
import com.home.opencarshare.model.pojo.Trip

@Composable
fun DriverCardContent(data: Driver) {
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    val mainPadding = dimensionResource(id = R.dimen.main_margin_compact)
    val textSize = 18.sp//dimensionResource(id = R.dimen.text_size)
    val textPadding = dimensionResource(id = R.dimen.text_padding)
    Column(
        modifier = Modifier
                .padding(
                        top = baselineGrid,
                        bottom = baselineGrid,
                        start = mainPadding,
                        end = mainPadding
                )
                .fillMaxWidth()
    ) {
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

@Composable
fun DriverCardContentEditable(onConfirmClick: (driver: DriverCredentials) -> Unit) {
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    val mainPadding = dimensionResource(id = R.dimen.main_margin_compact)
    var componentSpace = dimensionResource(id = R.dimen.component_space)

    var driverName by remember { mutableStateOf("") }
    var driverCell by remember { mutableStateOf("") }
    var driverSecret by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(mainPadding)) {
        TextFieldEditable(
            state = driverName,
            onTextChanged = { it -> driverName = it },
            hint = stringResource(id = R.string.create_trip_screen_driver_name),
            icon = null
        )
        TextFieldEditable(
            state = driverCell,
            onTextChanged = { it -> driverCell = it },
            hint = stringResource(id = R.string.create_trip_screen_driver_cell),
            icon = null
        )
        TextFieldEditable(
                state = driverSecret,
                onTextChanged = { it -> driverSecret = it },
                hint = stringResource(id = R.string.create_trip_screen_driver_secret),
                icon = null
        )
        Button(
            onClick = { onConfirmClick(DriverCredentials(name = driverName, cell = driverCell, secret = driverSecret)) },
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