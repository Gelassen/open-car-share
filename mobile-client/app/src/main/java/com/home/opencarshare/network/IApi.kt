package com.home.opencarshare.network

import com.home.opencarshare.model.pojo.*
import okhttp3.Credentials
import retrofit2.Response
import retrofit2.http.*

interface IApi {

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("/api/v1/driver")
    suspend fun createDriver(@Header("Authorization") credentials: String,
                             @Body driver: Driver): Response<DriverCreateApiResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("/api/v1/driver")
    suspend fun getDriver(@Header("Authorization") credentials: String): Response<DriverApiResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("/api/v1/driver/trips")
    suspend fun getDriverWithTrips(@Header("Authorization") credentials: String): Response<DriverApiResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("/api/v1/trips")
    suspend fun getTrips(
        @Query("locationFrom") locationFrom: String,
        @Query("locationTo") locationTo: String,
        @Query("time") time: Long
    ) : Response<TripsApiResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("/api/v1/trips")
    suspend fun getTripById(@Query("id") id: String): Response<TripsApiResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("/api/v1/trips")
    suspend fun getTripByDriver(@Header("Authorization") credentials: String): Response<TripsApiResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("/api/v1/trips/book")
    suspend fun bookTrip(@Query("id") tripId: String): Response<TripBookingApiResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("/api/v1/trips/create")
    suspend fun createTrip(@Body trip: Trip): Response<TripCreateApiResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @DELETE("/api/v1/trips")
    suspend fun cancelTrip(@Header("Authorization") credentials: String,
                           @Query("id") tripId: String) : Response<TripCancelApiResponse>
}