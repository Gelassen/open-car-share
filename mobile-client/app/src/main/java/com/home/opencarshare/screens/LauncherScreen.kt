package com.home.opencarshare.screens

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
import com.home.opencarshare.R
import com.home.opencarshare.screens.elements.SingleCard

@Composable
fun StartScreen(onDriverClick: () -> Unit, onPassengerClick: () -> Unit) {
    SingleCard(
        content = {
            StartScreenContent(
                onDriverClick = { onDriverClick() },
                onPassengerClick = { onPassengerClick() }
            )
        }
    )
}

@Composable
fun StartScreenContent(onDriverClick: () -> Unit, onPassengerClick: () -> Unit) {
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    val mainPadding = dimensionResource(id = R.dimen.main_margin_compact)
    Column(modifier = Modifier.padding(mainPadding)) {
        Button(
            onClick = { onDriverClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(baselineGrid)
                .height(dimensionResource(id = R.dimen.button_height)),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue, contentColor = Color.White),
        ) {
            Text(
                text = stringResource(id = R.string.start_screen_driver_button),
                modifier = Modifier.align(Alignment.CenterVertically),
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            onClick = { onPassengerClick() },
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
                text = stringResource(id = R.string.start_screen_passenger_button),
                modifier = Modifier.align(Alignment.CenterVertically),
                fontWeight = FontWeight.Bold
            )
        }
    }
}