package com.home.opencarshare.di

import com.home.opencarshare.model.pojo.Trip
import com.home.opencarshare.model.pojo.Driver
import com.home.opencarshare.providers.TripsProvider
import java.util.ArrayList

class Stubs {
    object Trips {
        var trips: MutableCollection<Trip> = ArrayList()
        fun generateTrip(): Trip {
            return Trip("SUID", "Kazan", "Moscow", TripsProvider().dateTimeAsLong("21.01.2022 8:00"), "2", "10", Driver())
        }

        init {
            trips.add(generateTrip())
            trips.add(generateTrip())
            trips.add(generateTrip())
            trips.add(generateTrip())
            trips.add(generateTrip())
            trips.add(generateTrip())
            trips.add(generateTrip())
            trips.add(generateTrip())
            trips.add(generateTrip())
            trips.add(generateTrip())
            trips.add(generateTrip())
            trips.add(generateTrip())
        }
    }
}