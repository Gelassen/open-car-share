package com.home.opencarshare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.home.opencarshare.R
import com.home.opencarshare.model.pojo.Trip

@Composable
fun TripsComposeList(
    state: List<Trip>,
    onClick: (String) -> Unit,
    modifier: Modifier
) {
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    val mainPadding = dimensionResource(id = R.dimen.main_margin_compact)
    val componentSpace = dimensionResource(id = R.dimen.component_space)
    val elevation = dimensionResource(id = R.dimen.elevation)
    val scrollState = rememberLazyListState()
    LazyColumn(
        state = scrollState,
        contentPadding = PaddingValues(horizontal = mainPadding, vertical = baselineGrid),
        modifier = modifier
    ) {
        items(state) { it ->
            TripViewItem(
                data = it,
                onClick = { tripId -> onClick(tripId) },
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .background(colorResource(id = R.color.white))
            )
        }
    }
}