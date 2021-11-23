package com.home.opencarshare.network

import com.home.opencarshare.model.TripsApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Repository @Inject constructor(val api: IApi) {

    fun getTrips(city: String, time: Long): Flow<TripsApiResponse> {
        return flow {
            api.getTrips(city, time)
        }
    }
}