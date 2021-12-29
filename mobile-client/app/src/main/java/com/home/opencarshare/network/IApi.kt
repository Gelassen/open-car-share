package com.home.opencarshare.network

import com.home.opencarshare.model.pojo.*
import okhttp3.Credentials
import retrofit2.Response
import retrofit2.http.*

interface IApi {

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("/api/driver")
    suspend fun createDriver(@Body driver: DriverCredentials): Response<DriverCreateApiResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("/api/driver")
    suspend fun getDriver(@Header("Authorization") credentials: String): Response<DriverApiResponse>

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
    @GET("/api/trips")
    suspend fun getTripByDriver(@Header("Authorization") credentials: String): Response<TripsApiResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("/api/trips/book")
    suspend fun bookTrip(@Query("id") tripId: String): Response<TripBookingApiResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("/api/trips/create")
    suspend fun createTrip(@Body trip: Trip): Response<TripCreateApiResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @DELETE("/api/trips")
    suspend fun cancelTrip(@Header("Authorization") credentials: String,
                           @Query("id") tripId: String) : Response<TripCancelApiResponse>
}