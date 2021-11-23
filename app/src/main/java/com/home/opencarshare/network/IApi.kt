package com.home.opencarshare.network

import com.home.opencarshare.model.TripsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface IApi {

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("/api/trips/{city}/{time}")
    suspend fun getTrips(@Path("city") city: String,
                 @Path("time") time: Long) : Response<TripsApiResponse>
}