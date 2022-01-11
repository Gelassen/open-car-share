package com.home.opencarshare.providers

import android.text.TextUtils
import android.util.Log
import androidx.compose.ui.text.intl.Locale
import com.home.opencarshare.App
import java.text.ParseException
import java.text.SimpleDateFormat

const val dateTimeFormat = "dd.MM.yyyy HH:mm"

class TripsProvider {

    fun dateTimeAsLong(input: String) : Long {
        var result: Long
        try {
            result = SimpleDateFormat(dateTimeFormat).parse(input).time
        } catch (ex: ParseException) {
            Log.e(App.TAG, "Failed to process exception", ex)
            result = System.currentTimeMillis()
        }
        return result
    }

    fun dateInUserFormat(date: Long): String {
        return SimpleDateFormat(dateTimeFormat).format(date)
    }

    fun validateLocation(input: String) : Boolean {
        return !TextUtils.isEmpty(input)
    }

    fun validateDate(input: String) : Boolean {
        // TODO complete me
        return false
    }

}