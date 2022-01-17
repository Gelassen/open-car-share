package com.home.opencarshare.screens.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import com.home.opencarshare.R

@Composable
fun SingleCard(content: @Composable() () -> Unit) {
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
                .wrapContentHeight(align = Alignment.CenterVertically)
                .padding(componentSpace),
            elevation = elevation
        ) {
            content()
        }
    }
}