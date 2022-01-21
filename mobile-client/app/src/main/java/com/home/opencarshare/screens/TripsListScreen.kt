package com.home.opencarshare.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.home.opencarshare.App
import com.home.opencarshare.R
import com.home.opencarshare.model.pojo.Trip
import com.home.opencarshare.screens.elements.SingleCard
import com.home.opencarshare.screens.viewmodel.PassengerTripUiState
import com.home.opencarshare.screens.viewmodel.TripsViewModel
import kotlinx.coroutines.launch

@Composable
fun TripListCard(state: PassengerTripUiState, viewModel: TripsViewModel) {
    var scope = rememberCoroutineScope()
    BackHandler {
        viewModel.onReturnBackFromListScreen()
    }
    SingleCard(
        content = {
            TripsComposeList(
                state = (state as PassengerTripUiState.TripsListUiState).trips,
                onClick = { tripId ->
                    scope.launch {
                        viewModel.getTripById(tripId)
                    }
                },
                modifier = Modifier)
        }
    )
}

@Composable
fun TripsComposeList(
    state: List<Trip>,
    onClick: (String) -> Unit,
    modifier: Modifier
) {
    Log.d(App.TAG, "[screen] TripsComposeList")
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    val mainPadding = dimensionResource(id = R.dimen.main_margin_compact)
    val componentSpace = dimensionResource(id = R.dimen.component_space)
    val elevation = dimensionResource(id = R.dimen.elevation)
    val scrollState = rememberLazyListState()
    if (state.isNotEmpty()) {
        Log.d(App.TAG, "[state] there are some items for list")
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
    } else {
        Log.d(App.TAG, "[state] there is no items for list")
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                text = stringResource(id = R.string.list_screen_no_items),
                textAlign = TextAlign.Center
            )
        }
    }

}