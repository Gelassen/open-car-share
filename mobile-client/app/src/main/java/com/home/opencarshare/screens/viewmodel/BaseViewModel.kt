package com.home.opencarshare.screens.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.home.opencarshare.R
import com.home.opencarshare.network.Response

abstract class BaseViewModel
constructor(
    private val context: Context
    ) : ViewModel() {

    protected fun getErrorMessage(errorResponse: Response.Error): String {
        var errorMessage = ""
        if (errorResponse is Response.Error.Exception) {
            errorMessage = context.resources.getString(R.string.default_error_message)
        } else {
            errorMessage = context.resources.getString(R.string.error_message_with_server_response, (errorResponse as Response.Error.Message).msg)
        }
        return errorMessage
    }
}