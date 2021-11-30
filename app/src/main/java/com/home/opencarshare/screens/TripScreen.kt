package com.home.opencarshare.screens

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.home.opencarshare.model.Trip
import com.home.opencarshare.network.Response
import com.home.opencarshare.ui.theme.OpenCarShareTheme

@Composable
fun TripScreen(viewModel: TripsViewModel, navController: NavController) {
    // TODO
    //  work with a view model was rethink in compose,
    //  when State is for UI logic view model is for business logic,
    //  UI operates with state and invokes view model methods as like as a view use presenter,
    //  the question is how does State is updated and compose is notified about this
    val state by viewModel.trips.collectAsState()
    LaunchedEffect(viewModel) {
        viewModel.getTrips("", System.currentTimeMillis())
    }
    OpenCarShareTheme {
        Surface(color = MaterialTheme.colors.background) {
            TripsComposeList(state = state, navController = navController/*, modifier = Modifier.padding(16.dp)*/)
        }
    }
}

@Composable
fun TripsComposeList(
    state: Response<List<Trip>>,
    navController: NavController
) {
    val activity = LocalContext.current as ComponentActivity
    val scrollState = rememberLazyListState()
/*    activity.lifecycleScope.launch {

    }*/
    LazyColumn(state = scrollState, contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        /*modifier = modifier.padding(16.dp)*/) {
        when (state) {
            is Response.Data -> {
                items(state.data) { it ->
                    TripViewItem(data = it, navController = navController, modifier = Modifier.padding(vertical = 2.dp))
                }
            }
            is Response.Error -> {
                // TODO show error (also consider case when you have to clear the list from previous data)
            }
        }
    }
}

