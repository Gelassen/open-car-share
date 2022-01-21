package com.home.opencarshare.converters

import androidx.navigation.NavBackStackEntry
import com.home.opencarshare.model.pojo.Trip
import android.os.Bundle
import com.home.opencarshare.navigation.AppNavigation
import java.lang.IllegalArgumentException

class ArgsToTripConverter {

    fun convertArgsToTrip(navBackStackEntry: NavBackStackEntry): Trip {
        return convertArgsToTrip(navBackStackEntry.arguments)
    }

    fun convertArgsToTrip(args: Bundle?): Trip {
        if (args == null || args.isEmpty)
            throw IllegalArgumentException("Args are null. Did you forget to pass them into compose view?")
        return Trip(
            locationFrom = args.getString(AppNavigation.Trips.ARG_TRIP_LOCATION_FROM, ""),
            locationTo = args.getString(AppNavigation.Trips.ARG_TRIP_LOCATION_TO, ""),
            date = args.getLong(AppNavigation.Trips.ARG_TRIP_DATE, 0L)
        )
    }
}