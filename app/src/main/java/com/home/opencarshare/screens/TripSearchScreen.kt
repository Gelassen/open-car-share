package com.home.opencarshare.screens


import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.home.opencarshare.R

@Composable
fun TripSearchScreen(onSearchClick: (locationFrom: String, locationTo: String, date: String) -> Unit) {
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    val mainPadding = dimensionResource(id = R.dimen.main_margin_compact)
    val componentSpace = dimensionResource(id = R.dimen.component_space)
    val elevation = dimensionResource(id = R.dimen.elevation)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.colorPrimaryDark))
            .padding(
                paddingValues = PaddingValues(
                    horizontal = mainPadding,
                    vertical = baselineGrid
                )
            )
    ) {
        Card(
            backgroundColor = MaterialTheme.colors.background,
            modifier = Modifier
                .wrapContentHeight(align = CenterVertically)
                .padding(componentSpace),
            elevation = elevation
        ) {
            // TODO figure out is there existing composable for date picker and numeric picker
            var locationFromTxt by remember { mutableStateOf("") }
            var locationToTxt by remember { mutableStateOf("") }
            var pickUpDate by remember { mutableStateOf("")}
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
                    onClick = { onSearchClick(locationFromTxt, locationToTxt, pickUpDate) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(baselineGrid)
                        .height(dimensionResource(id = R.dimen.button_height)),
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
fun TripSearchRow(state: String, onTextChanged: (String) -> Unit, hint: String, icon: ImageVector? ) {
    val baselineGrid = dimensionResource(id = R.dimen.baseline_grid)
    if (icon == null) {
        TextField(
            value = state,
            onValueChange = { onTextChanged(it) },
            modifier = Modifier
                .padding(baselineGrid)
                .fillMaxWidth(),
            placeholder = { Text("$hint") },
        )
    } else {
        TextField(
            value = state,
            onValueChange = { onTextChanged(it) },
            modifier = Modifier
                .padding(baselineGrid)
                .fillMaxWidth(),
            placeholder = { Text("$hint") },
            leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        )
    }
}