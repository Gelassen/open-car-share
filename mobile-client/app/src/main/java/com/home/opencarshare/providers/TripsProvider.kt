package com.home.opencarshare.providers

import android.text.TextUtils

class TripsProvider {

    fun validateLocation(input: String) : Boolean {
        return !TextUtils.isEmpty(input)
    }

    fun validateDate(input: String) : Boolean {
        // TODO complete me
        return false
    }
}