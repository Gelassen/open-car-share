package com.home.opencarshare.navigation

object AppNavigation {

    object Start {
        const val START = "start"
    }

    object Create {
        const val CREATE = "tripCreate"
    }

    object Search {
        const val TRIP_SEARCH = "tripSearch"
        const val ARG_TRIP_ID = "tripId"
    }

    object Booking {
        const val TRIP_BOOKING = "tripBooking"
        const val ARG_TRIP_ID = Search.ARG_TRIP_ID
    }

    object Trips {
        const val TRIPS = "trips"
        const val ARG_TRIP_LOCATION_FROM = "locationFrom"
        const val ARG_TRIP_LOCATION_TO = "locationTo"
        const val ARG_TRIP_DATE = "date"
    }
}