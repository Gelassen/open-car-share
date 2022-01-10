package com.home.opencarshare.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.home.opencarshare.R
import com.home.opencarshare.model.pojo.Trip

@Composable
fun TripViewItem(data: Trip,
                 onClick: (String) -> Unit,
                 modifier: Modifier) {
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    val mainPadding = dimensionResource(id = R.dimen.main_margin_compact)
    val textSize = 18.sp//dimensionResource(id = R.dimen.text_size)
    val textPadding = dimensionResource(id = R.dimen.text_padding)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(data.id) }
    ) {
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
                    modifier = Modifier.padding(textPadding),
                    fontSize = textSize)
                Text(text = " -- ")
                Text(
                    text = data.locationTo,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(textPadding),
                    fontSize = textSize)
            }
            Column() {
                Text(
                    text = data.date,
                    modifier = Modifier.padding(textPadding),
                    fontSize = textSize)
                Text(
                    text = "available seats: ${data.availableSeats}",
                    modifier = Modifier.padding(textPadding),
                    fontSize = textSize)
            }
        }
    }

}

private fun Int.textDp(density: Density): TextUnit = with(density) {
    this@textDp.dp.toSp()
}


val Int.textDp: TextUnit
    @Composable get() =  this.textDp(density = LocalDensity.current)