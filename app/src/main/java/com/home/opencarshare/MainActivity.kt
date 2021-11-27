package com.home.opencarshare

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.home.opencarshare.di.Stubs
import com.home.opencarshare.model.Trip
import com.home.opencarshare.network.Repository
import com.home.opencarshare.ui.theme.OpenCarShareTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repo : Repository

    private val stub: List<Trip> = ArrayList(Stubs.Trips.trips)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())

        setContent {
            TripScreen(data = stub)
        }

/*        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                repo.getTrips("Moscow", Date("Wed, 4 Jul 2001 12:08:56 -0700").time)
                    .catch { ex -> Log.e("TAG", "Something went wrong", ex) }
                    .collect { it ->
                        Log.d("TAG", "Response: " + it)
                    }
            }
        }*/
    }
}

@Composable
fun TripDetails(data: Trip) {
    Column {
        TripViewItem(data = data, modifier = Modifier)
        Text(text = "testing Composable extensions")
    }
}

@Composable
fun TripScreen(data: List<Trip>) {
    OpenCarShareTheme {
        Surface(color = MaterialTheme.colors.background) {
            TripsComposeList(data = data/*, modifier = Modifier.padding(16.dp)*/)
        }
    }
}

@Composable
fun TripViewItem(data: Trip, modifier: Modifier) {
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

//@Preview(showBackground = true, name = "Text preview")
//@Preview(showBackground = true, name = "Trips")
@Composable
fun TripsComposeList(
    modifier: Modifier = Modifier,
    data: List<Trip>,
) {
    val context = LocalContext.current
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState, contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        /*modifier = modifier.padding(16.dp)*/) {
        items(data) { it ->
            TripViewItem(data = it, modifier = modifier.padding(vertical = 2.dp))
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OpenCarShareTheme {
        Greeting("Android")
    }
}