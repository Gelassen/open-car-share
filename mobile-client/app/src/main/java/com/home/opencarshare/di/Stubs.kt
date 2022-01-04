package com.home.opencarshare.di

import com.home.opencarshare.model.pojo.Trip
import com.home.opencarshare.model.pojo.Driver
import java.util.ArrayList

class Stubs {
    object Trips {
        var trips: MutableCollection<Trip> = ArrayList()
        fun generateTrip(): Trip {
            return Trip("SUID", "Kazan", "Moscow", "Wed, 4 Jul 2001 12:08:56 -0700", "2", "10", Driver())
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