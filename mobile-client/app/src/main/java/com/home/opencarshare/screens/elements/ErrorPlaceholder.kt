package com.home.opencarshare.screens.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.home.opencarshare.R

@Composable
fun ErrorPlaceholder(text: String) {
    val mainPadding = dimensionResource(id = R.dimen.main_margin_compact)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(start = mainPadding, end = mainPadding)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center
        )
    }
}