package com.home.opencarshare.network

import com.home.opencarshare.model.TripsApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Repository @Inject constructor(val api: IApi) {

    fun getTrips(city: String, time: Long): Flow<Response<TripsApiResponse>> {
        return flow {
            val response = api.getTrips(city, time)
            if (response.isSuccessful) {
                emit(Response.Data(response.body()!!))
            } else {
                emit(Response.Error.Message(response.message()))
            }
        }
            .catch { ex ->
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
}