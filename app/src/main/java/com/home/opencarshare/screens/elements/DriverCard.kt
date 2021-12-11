package com.home.opencarshare.screens.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.home.opencarshare.R
import com.home.opencarshare.model.pojo.Driver

@Composable
fun DriverCard(data: Driver) {
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
fun DriverCardEditable() {
    
}