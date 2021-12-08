package com.home.opencarshare.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.home.opencarshare.R

@Composable
fun TripSearchScreen(onSearchClick: (locationFrom: String, locationTo: String, date: String) -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(paddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp))
    ) {
        Card(
            backgroundColor = MaterialTheme.colors.background,
            modifier = Modifier.wrapContentHeight(align = CenterVertically).padding(24.dp),
            elevation = 2.dp
        ) {
            // TODO figure out is there existing composable for date picker and numeric picker
            var locationFromTxt by remember { mutableStateOf("") }
            var locationToTxt by remember { mutableStateOf("") }
            var pickUpDate by remember { mutableStateOf("")}
            Column(modifier = Modifier.padding(16.dp)) {
                TripSearchRow(
                    state = locationFromTxt,
                    onTextChanged = { it -> locationFromTxt = it },
                    hint = stringResource(id = R.string.search_screen_location_to_hint)
                )
                TripSearchRow(
                    state = locationToTxt,
                    onTextChanged = { it -> locationToTxt = it },
                    hint = stringResource(id = R.string.search_screen_location_from_hint)
                )
                TripSearchRow(
                    state = pickUpDate,
                    onTextChanged = { it -> pickUpDate = it },
                    hint = stringResource(id = R.string.search_screen_date)
                )
                Button(
                    onClick = { onSearchClick(locationFromTxt, locationToTxt, pickUpDate) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = buttonColors(backgroundColor = Color.Blue, contentColor = Color.White),
                ) {
                    Text(
                        text = stringResource(id = R.string.search_screen_confirm_button),
                        modifier = Modifier.align(CenterVertically),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TripSearchRow(state: String, onTextChanged: (String) -> Unit, hint: String) {
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