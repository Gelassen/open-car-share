package com.home.opencarshare.screens.elements

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.home.opencarshare.App
import com.home.opencarshare.R
import com.home.opencarshare.network.Response
import com.home.opencarshare.screens.viewmodel.BaseViewModel
import com.home.opencarshare.screens.viewmodel.TripsViewModel

@Deprecated(message = "Use ErrorCard(state: String, viewModel: BaseViewModel) instead")
@Composable
fun ErrorCard(state: String) {
    var errorMessage: String = state
    SingleCard(
            content = {
                ErrorPlaceholder(text = errorMessage)
            }
    )
    Toast.makeText(LocalContext.current, "You are on error card", Toast.LENGTH_SHORT).show()
}

@Composable
fun ErrorCard(state: String, viewModel: BaseViewModel) {
    var errorMessage: String = state
    BackHandler {
        Log.d(App.TAG, "[action] on back pressed is called")
        when(viewModel) {
            is TripsViewModel -> {
                viewModel.errorShown(errorMessage)
            }
        }
    }

    SingleCard(
        content = {
            ErrorPlaceholder(text = errorMessage)
        }
    )
}