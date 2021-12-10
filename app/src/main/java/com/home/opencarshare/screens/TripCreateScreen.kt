package com.home.opencarshare.screens

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.home.opencarshare.R
import com.home.opencarshare.screens.elements.SingleCard
import com.home.opencarshare.screens.elements.TripSearchRow
import com.home.opencarshare.screens.viewmodel.TripsViewModel


@Composable
fun TripCreateScreen(viewModel: TripsViewModel) {
    LaunchedEffect(viewModel) {
        viewModel.tripBookState
    }

    SingleCard(content = { TripCreateScreenContent(
        onCreateClick = { locationFromTxt, locationToTxt, pickUpDate ->
            // TODO complete me
        })
    })
}

@Composable
fun TripCreateScreenContent(onCreateClick: (locationFrom: String, locationTo: String, date: String) -> Unit) {
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    var locationFromTxt by remember { mutableStateOf("") }
    var locationToTxt by remember { mutableStateOf("") }
    var pickUpDate by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TripSearchRow(
            state = locationFromTxt,
            onTextChanged = { it -> locationFromTxt = it },
            hint = stringResource(id = R.string.search_screen_location_to_hint),
            icon = Icons.Default.LocationOn
        )
        TripSearchRow(
            state = locationToTxt,
            onTextChanged = { it -> locationToTxt = it },
            hint = stringResource(id = R.string.search_screen_location_from_hint),
            icon = Icons.Default.LocationOn
        )
        TripSearchRow(
            state = pickUpDate,
            onTextChanged = { it -> pickUpDate = it },
            hint = stringResource(id = R.string.search_screen_date),
            icon = null
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
                text = stringResource(id = R.string.search_screen_confirm_button),
                modifier = Modifier.align(Alignment.CenterVertically),
                fontWeight = FontWeight.Bold
            )
        }
    }
}