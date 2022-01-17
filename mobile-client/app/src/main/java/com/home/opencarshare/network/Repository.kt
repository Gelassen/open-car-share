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
import java.lang.Exception
import javax.inject.Inject

class Repository @Inject constructor(val api: IApi) {

    fun createDriver(driverCredentials: DriverCredentials): Flow<Response<ServiceMessage>> {
        return flow {
            val response = api.createDriver(
                credentials = Credentials.basic(driverCredentials.cell, driverCredentials.secret),
                driver = driverCredentials.toDriver()
            )
            if (response.isSuccessful) {
                val payload = response.body()!!
                payload.message
                if (payload.code.toInt() == 200) {
                    emit(Response.Data(payload.result))
                } else {
                    emit(Response.Error.Message("${payload.code}: ${payload.message}"))
                }
            } else {
                emit(Response.Error.Message(response.message()))
            }
        }
            .catch { ex ->
                Response.Error.Exception(ex)
            }
    }

    fun getTrips(locationFrom: String, locationTo: String, time: Long): Flow<Response<List<Trip>>> {
        Log.d(App.TAG,"[action] get trips - start")
        return flow {
            try {
                var response = api.getTrips(locationFrom, locationTo, time)
            if (response.isSuccessful) {
                val payload = response.body()!!
                if (payload.code.toInt() == 200) {
                    Log.d(App.TAG, "payload $payload.result")
                    emit(Response.Data(payload.result))
                } else {
                    emit(Response.Error.Message("${payload.code}: ${payload.message}"))
                }
            } else {
                emit(Response.Error.Message(response.message()))
            }
            } catch (ex: Exception) {
                Log.e(App.TAG, "Something wend wrong due network request", ex)
                emit(Response.Error.Exception(ex))
            }
            Log.d(App.TAG, "get trips - end")
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
                Log.d(App.TAG, "getTripById() response $payload")
                if (payload.code.toInt() == 200) {
                    emit(Response.Data(payload.result.first()))
                } else {
                    emit(Response.Error.Message("${payload.code}: ${payload.message}"))
                }
            } else {
                emit(Response.Error.Message(response.message()))
            }
        }
            .catch { ex ->
                Response.Error.Exception(ex)
            }
    }

    @Deprecated("API endpoint used by this methods is disabled")
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
                if (payload.code.toInt() == 200) {
                    emit(Response.Data(payload.result))
                } else {
                    emit(Response.Error.Message("${payload.code}: ${payload.message}"))
                }
            } else {
                emit(Response.Error.Message(response.message()))
            }
        }
    }

    fun getDriverWithTrips(cell: String, secret: String): Flow<Response<DriverCredentials>> {
        return flow {
            Log.d(App.TAG, "Request driver with trips from server")
            val response = api.getDriverWithTrips(credentials = Credentials.basic(cell, secret))
            if (response.isSuccessful) {
                val payload = response.body()!!
                if (payload.code.toInt() == 200) {
                    emit(Response.Data(payload.result))
                } else {
                    emit(Response.Error.Message("${payload.code}: ${payload.message}"))
                }
            } else {
                emit(Response.Error.Message(response.message()))
            }
        }
    }

    fun getTripsByDriver(cell: String, secret: String): Flow<Response<List<Trip>>> {
        return flow {
            val response = api.getTripByDriver(credentials = Credentials.basic(cell, secret))
            if (response.isSuccessful) {
                val payload = response.body()!!
                if (payload.code.toInt() == 200) {
                    emit(Response.Data(payload.result))
                } else {
                    emit(Response.Error.Message("${payload.code}: ${payload.message}"))
                }
            } else {
                emit(Response.Error.Message(response.message()))
            }
        }
    }

    fun cancelTrip(tripId: String, cell: String, secret: String): Flow<Response<ServiceMessage>> {
        if (tripId.isEmpty())
            throw IllegalArgumentException("Trip id for cancellation is empty. Does your trip have correct one?")
        return flow {
            val response = api.cancelTrip(
                credentials = Credentials.basic(cell, secret),
                tripId = tripId
            )
            if (response.isSuccessful) {
                val payload = response.body()!!
                if (payload.code.toInt() == 200) {
                    emit(Response.Data(payload.result))
                } else {
                    emit(Response.Error.Message("${payload.code}: ${payload.message}"))
                }
            } else {
                emit(Response.Error.Message(response.message()))
            }
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