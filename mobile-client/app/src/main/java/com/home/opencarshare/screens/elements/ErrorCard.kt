package com.home.opencarshare.screens.elements

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.home.opencarshare.App
import com.home.opencarshare.R
import com.home.opencarshare.network.Response

@Composable
fun ErrorCard(state: String) {
    var errorMessage: String = state
/*    if (state is Response.Error.Exception) {
        Log.e(App.TAG, "Driver state is error", (state as Response.Error.Exception).error)
        errorMessage = stringResource(id = R.string.default_error_message)
    } else {
        errorMessage = stringResource(id = R.string.error_message_with_server_response, (state as Response.Error.Message).msg)
    }*/
    SingleCard(
            content = {
                ErrorPlaceholder(text = errorMessage)
            }
    )
    Toast.makeText(LocalContext.current, "You are on error card", Toast.LENGTH_SHORT).show()
    // TODO on back navigation don't forget update error message by removing the last error
}