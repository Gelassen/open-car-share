package com.home.opencarshare.network

import com.home.opencarshare.model.ServiceMessage
import com.home.opencarshare.model.Trip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Repository @Inject constructor(val api: IApi) {

    fun getTrips(locationFrom: String, locationTo: String, time: Long): Flow<Response<List<Trip>>> {
        return flow {
            val response = api.getTrips(locationFrom, locationTo, time)
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

}

sealed class Response<out T: Any> {
    data class Data<out T: Any>(val data: T): Response<T>()
    sealed class Error: Response<Nothing>() {
        data class Exception(val error: Throwable): Error()
        data class Message(val msg: String): Error()
    }
    data class Loading<out T: Any>(val stub: T): Response<T>()
}