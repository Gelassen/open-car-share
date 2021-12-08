package com.home.opencarshare.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.home.opencarshare.R
import com.home.opencarshare.model.Trip

@Composable
fun TripViewItem(data: Trip,
                 navigateToBooking: (String) -> Unit,
                 modifier: Modifier) {
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    val mainPadding = dimensionResource(id = R.dimen.main_margin_compact)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToBooking(data.id) }) {
        Column(
            modifier = modifier
                .padding(
                    top = baselineGrid,
                    bottom = baselineGrid,
                    start = mainPadding,
                    end = mainPadding
                )
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = data.locationFrom,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(2.dp),
                    fontSize = 18.sp)
                Text(text = " -- ")
                Text(
                    text = data.locationTo,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(2.dp),
                    fontSize = 18.sp)
            }
            Column() {
                Text(
                    text = data.date,
                    modifier = Modifier.padding(2.dp),
                    fontSize = 18.sp)
                Text(
                    text = "available seats: ${data.availableSeats}",
                    modifier = Modifier.padding(2.dp),
                    fontSize = 18.sp)
            }
        }
    }

}