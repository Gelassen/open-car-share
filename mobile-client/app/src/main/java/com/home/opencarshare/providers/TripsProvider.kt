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
            result = -1L
        }
        return result
    }

    fun dateInUserFormat(date: Long): String {
        return SimpleDateFormat(dateTimeFormat).format(date)
    }

    fun validateLocation(input: String) : Boolean {
        return !TextUtils.isEmpty(input)
    }

    /**
     * Method expects date in format 01.01.2020 07:00, but also correctly parse in format 1.01.2020 7:00.
     *
     * This is the cause why comparing by date as long is safe instead of by date as string.
     * */
    fun validateDate(input: String) : Boolean {
        if (input.isBlank())
            return false

        val parsedDateTime = dateTimeAsLong(input)
        if (parsedDateTime == -1L)
            return false

        val restoredDateTime = dateInUserFormat(parsedDateTime)
        val parsedAgain = dateTimeAsLong(restoredDateTime)
        return parsedDateTime.equals(parsedAgain)
    }

}