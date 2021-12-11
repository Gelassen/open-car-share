package com.home.opencarshare.screens.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import com.home.opencarshare.R

@Composable
fun TextFieldEditable(state: String, onTextChanged: (String) -> Unit, hint: String, icon: ImageVector? ) {
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