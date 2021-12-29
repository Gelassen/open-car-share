package com.home.opencarshare.network

import android.util.Log
import com.home.opencarshare.App
import com.home.opencarshare.model.pojo.Driver
import com.home.opencarshare.model.pojo.DriverCredentials
import com.home.opencarshare.model.pojo.ServiceMessage
import com.home.opencarshare.model.pojo.Trip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.Credentials
import javax.inject.Inject

class Repository @Inject constructor(val api: IApi) {

    fun createDriver(driverCredentials: DriverCredentials): Flow<Response<ServiceMessage>> {
        return flow {
            val response = api.createDriver(driverCredentials)
            if (response.isSuccessful) {
                val payload = response.body()!!
                payload.message
                if (payload.code.toInt() != 200) {
                    throw IllegalStateException(
                            "System doesn't support any other states except code 200, " +
                                    "but has received ${payload.code}")
                }
                emit(Response.Data(payload.result))
            } else {
                emit(Response.Error.Message(response.message()))
            }
        }
            .catch { ex ->
                Response.Error.Exception(ex)
            }
    }

    fun getTrips(locationFrom: String, locationTo: String, time: Long): Flow<Response<List<Trip>>> {
        return flow {
            val response = api.getTrips(locationFrom, locationTo, time)
            if (response.isSuccessful) {
                val payload = response.body()!!
                payload.message
                if (payload.code.toInt() != 200) {
                    throw IllegalStateException(
                        "System doesn't support any other states except code 200, " +
                                "but has received ${payload.code}")
                }
                emit(Response.Data(payload.result))
            } else {
                emit(Response.Error.Message(response.message()))
            }
        }
            .catch { ex ->
                Response.Error.Exception(ex)
            }

    }

    fun getTripById(id: String) : Flow<Response<Trip>> {
        return flow {
            val response = api.getTripById(id)
            if (response.isSuccessful) {
                val payload = response.body()!!
                if (payload.code.toInt() != 200) {
                    throw IllegalStateException(
                        "System doesn't support any other states except code 200, " +
                                "but has received ${payload.code}")
                }
                emit(Response.Data(payload.result.first()))
            } else {
                emit(Response.Error.Message(response.message()))
            }
        }
            .catch { ex ->
                Response.Error.Exception(ex)
            }
    }

    fun bookTrip(tripId: String) : Flow<Response<ServiceMessage>> {
        return flow {
            val response = api.bookTrip(tripId)
            if (response.isSuccessful) {
                val payload = response.body()!!
                if (payload.code.toInt() != 200) {
                    throw IllegalStateException(
                        "System doesn't support any other states except code 200, " +
                                "but has received ${payload.code}")
                }
                emit(Response.Data(payload.result))
            } else {
                emit(Response.Error.Message(response.message()))
            }
        }.catch { ex ->
            Response.Error.Exception(ex)
        }
    }

    fun createTrip(trip: Trip) : Flow<Response<ServiceMessage>> {
        return flow {
            val response = api.createTrip(trip = trip)
            if (response.isSuccessful) {
                val payload = response.body()!!
                if (payload.code.toInt() != 200) {
                    throw IllegalStateException(
                        "System doesn't support any other states except code 200, " +
                                "but has received ${payload.code}")
                }
                emit(Response.Data(payload.result))
            } else {
                emit(Response.Error.Message(response.message()))
            }
        }
    }

    fun getDriver(cell: String, secret: String): Flow<Response<DriverCredentials>> {
        return flow {
            Log.d(App.TAG, "Request driver from server")
            val response = api.getDriver(credentials = Credentials.basic(cell, secret))
            if (response.isSuccessful) {
                val payload = response.body()!!
                if (payload.code.toInt() != 200) {
                    throw IllegalStateException(
                            "System doesn't support any other states except code 200, " +
                                    "but has received ${payload.code}")
                }
                emit(Response.Data(payload.result))
            } else {
                emit(Response.Error.Message(response.message()))
            }
        }
    }

    var tripsByDriver = mutableListOf<Trip>()

    init {
        tripsByDriver.add(
            Trip(
                id = "SUID",
                locationFrom = "Kazan",
                locationTo = "Moscow",
                date = "Wed, 4 Jul 2001 12:08:56 -0700",
                availableSeats = "2",
                driver = Driver(id = "email", name = "Joe Dow", tripsCount = "31", cell = "+79101900122")
            )
        )
        tripsByDriver.add(
            Trip(
                id = "SUID2",
                locationFrom = "Kazan",
                locationTo = "Moscow",
                date = "Wed, 4 Jul 2001 12:08:56 -0700",
                availableSeats = "1",
                driver = Driver(id = "email", name = "Joe Dow", tripsCount = "31", cell = "+79101900122")
            )
        )
    }

    fun getTripsByDriver(cell: String, secret: String): Flow<Response<List<Trip>>> {
        return flow {
/*            val response = api.getTripByDriver(credentials = Credentials.basic(cell, secret))
            if (response.isSuccessful) {
                val payload = response.body()!!
                if (payload.code.toInt() != 200) {
                    throw IllegalStateException(
                        "System doesn't support any other states except code 200, " +
                                "but has received ${payload.code}")
                }
                emit(Response.Data(payload.result))
            } else {
                emit(Response.Error.Message(response.message()))
            }*/
            emit(Response.Data(tripsByDriver))
        }
    }

    fun cancelTrip(tripId: String, cell: String, secret: String): Flow<Response<ServiceMessage>> {
        return flow {
/*            val response = api.cancelTrip(
                credentials = Credentials.basic(cell, secret),
                tripId = tripId
            )
            if (response.isSuccessful) {
                val payload = response.body()!!
                if (payload.code.toInt() != 200) {
                    throw IllegalStateException(
                        "System doesn't support any other states except code 200, " +
                                "but has received ${payload.code}")
                }
                emit(Response.Data(payload.result))
            } else {
                emit(Response.Error.Message(response.message()))
            }*/
            tripsByDriver = tripsByDriver.filter { it.id != tripId } as MutableList<Trip>
            Log.d(App.TAG, "[action] repository::trips ${tripsByDriver.count()}")
            emit(Response.Data(ServiceMessage(status = ServiceMessage.Status.SUCCEED)))
        }
    }

}

sealed class Response<out T: Any> {
    data class Data<out T: Any>(val data: T): Response<T>()
    sealed class Error: Response<Nothing>() {
        data class Exception(val error: Throwable): Error()
        data class Message(val msg: String): Error()
    }
    data class Loading<Boolean>(val isLoading: Boolean): Response<kotlin.Boolean>()
}