package com.home.opencarshare.screens

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.View
import android.widget.CalendarView
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.home.opencarshare.model.Trip
import com.home.opencarshare.screens.viewmodel.TripsViewModel
import com.home.opencarshare.ui.theme.OpenCarShareTheme
import java.util.*

@Composable
fun TripSearchScreen(onSearchClick: (locationFrom: String, locationTo: String, date: String) -> Unit) {
    OpenCarShareTheme {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(paddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp))
        ) {
            Card(
                backgroundColor = MaterialTheme.colors.background,
                modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically),
                elevation = 2.dp
            ) {
                Column() {
                    // TODO figure out is there existing composable for date picker and numeric picker
                    var locationFromTxt by remember { mutableStateOf("") }
                    var locationToTxt by remember { mutableStateOf("") }
                    var pickUpDate by remember { mutableStateOf("")}
                    Column() {
                        TripSearchRow(
                            state = locationFromTxt,
                            onTextChanged = { it -> locationFromTxt = it },
                            hint = "location from"
                        )
//                        Divider(modifier = Modifier.height(1.dp))
                        TripSearchRow(
                            state = locationToTxt,
                            onTextChanged = { it -> locationToTxt = it },
                            hint = "location to"
                        )
                        TripSearchRow(
                            state = pickUpDate,
                            onTextChanged = { it -> pickUpDate = it },
                            hint = "pick up date"
                        )
                    }
                    Button(
                        onClick = { onSearchClick(locationFromTxt, locationToTxt, pickUpDate) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = buttonColors(backgroundColor = Color.Blue, contentColor = Color.White),
                    ) {
                        Text(
                            text = "Search",
                            modifier = Modifier.align(CenterVertically),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TripSearchRow(state: String, onTextChanged: (String) -> Unit, hint: String) {
    // TODO expose state on level higher, right now it is not clear would be state changing if it will be passed as a parameter
    TextField(
        value = state,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        placeholder = { Text("$hint") },
        leadingIcon = { Icon(imageVector = Icons.Default.LocationOn, contentDescription = null) },
    )
}