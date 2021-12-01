package com.home.opencarshare.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.home.opencarshare.model.Trip

@Composable
fun TripViewItem(data: Trip,
                 navigateToBooking: (String) -> Unit,
                 modifier: Modifier) {
    val context = LocalContext.current
    Surface(modifier = modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colors.background)
        .clickable {
            Toast
                .makeText(
                    context,
                    "On click action ${data.locationFrom} to ${data.locationTo}",
                    Toast.LENGTH_SHORT
                )
                .show()
            navigateToBooking(data.id)
        }
    ) {
        Column(modifier = modifier.padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = data.locationFrom, fontWeight = FontWeight.Bold)
                Text(text = " -- ")
                Text(text = data.locationTo, fontWeight = FontWeight.Bold)
            }
            Column() {
                Text(text = data.date)
                Text(text = "available seats: ${data.availableSeats}")
            }
        }
    }
}