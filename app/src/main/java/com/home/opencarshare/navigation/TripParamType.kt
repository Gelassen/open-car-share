package com.home.opencarshare.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.home.opencarshare.model.Trip

class TripParamType: NavType<Trip>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Trip? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): Trip {
        return Gson().fromJson(value, Trip::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: Trip) {
        bundle.putParcelable(key, value)
    }
}