package com.home.opencarshare.network

import com.home.opencarshare.model.pojo.*
import retrofit2.Response
import retrofit2.http.*

interface IApi {

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("/api/trips")
    suspend fun getTrips(
        @Query("locationFrom") locationFrom: String,
        @Query("locationTo") locationTo: String,
        @Query("time") time: Long
    ) : Response<TripsApiResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("/api/trips")
    suspend fun getTripById(@Query("id") id: String): Response<TripsApiResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("/api/trips/book")
    suspend fun bookTrip(@Query("id") tripId: String): Response<TripBookingApiResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("/api/trips/create")
    suspend fun createTrip(@Body trip: Trip): Response<TripCreateApiResponse>
}