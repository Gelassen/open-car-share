package com.home.opencarshare.network

import android.content.Context
import android.net.Uri
import okhttp3.*
import java.lang.IllegalArgumentException

class MockInterceptor(val context: Context): Interceptor {

    companion object {

        private const val NO_QUERY = ""

        private const val URL_TRIP = "/api/trips?locationTo=someCity&locationFrom=anotherCity&time=10000000"

        private const val URL_TRIP_BY_ID = "/api/trips?id=101"

        private const val URL_TRIP_BY_DRIVER = "/api/trips"

        private const val URL_TRIP_BOOKING = "/api/trips/book?id=101"

        private const val URL_TRIP_CREATE = "/api/trips/create"

        private const val URL_DRIVER = "/api/driver"

        private const val URL_DRIVER_CREATE = "/api/driver"
    }

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        val url = request.url().uri().toString()

        val msg = getMessage(getMockFileName(url, request.method()!!))
        val body = getBody(msg)

        return okhttp3.Response.Builder()
            .request(chain.request())
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message(msg)
            .body(body)
            .addHeader("content-type", "application/json")
            .build()
    }

    private fun getMessage(jsonName: String): String {
        return context.assets.open(jsonName).bufferedReader().use { it.readText() }
    }

    private fun getBody(msg: String): ResponseBody {
        return ResponseBody.create(
            MediaType.parse("application/json"),
            msg.toByteArray()
        )
    }

    private fun getMockFileName(url: String, method: String): String {
        lateinit var msg: String
        val uri = Uri.parse(url)
        if (matchPath(uri.pathSegments, URL_TRIP.split("?").first())
            && matchQuery(uri.queryParameterNames, URL_TRIP.split("?").last())
            && method.equals("GET")) {
            msg = "mock_api_trips_response.json"
        } else if (matchPath(uri.pathSegments, URL_TRIP_BY_ID.split("?").first())
            && matchQuery(uri.queryParameterNames, URL_TRIP_BY_ID.split("?").last())
            && method.equals("GET")) {
            msg = "mock_api_trip_by_id_response.json"
        } else if (matchPath(uri.pathSegments, URL_TRIP_BY_ID.split("?").first())
            && matchQuery(uri.queryParameterNames, URL_TRIP_BY_ID.split("?").last())
            && method.equals("DELETE")) {
            msg = "mock_api_trip_cancel_response.json"
        } else if (matchPath(uri.pathSegments, URL_TRIP_BOOKING.split("?").first())
            && matchQuery(uri.queryParameterNames, URL_TRIP_BOOKING.split("?").last())
            && method.equals("POST")) {
            msg = "mock_api_trip_book_positive_response.json"
        } else if (matchPath(uri.pathSegments, URL_TRIP_CREATE.split("?").first())
            /*&& matchQuery(uri.queryParameterNames, URL_TRIP_CREATE.split("?").last())*/
            && method.equals("POST")) {
            msg = "mock_api_trip_create_response.json"
        } else if (matchPath(uri.pathSegments, URL_DRIVER_CREATE.split("?").first())
                && method.equals("POST")) {
            msg = "mock_api_driver_create_response.json"
        } else if (matchPath(uri.pathSegments, URL_DRIVER.split("?").first())
                && method.equals("GET")) {
            msg = "mock_api_driver_get_response.json"
        } else if (matchPath(uri.pathSegments, URL_TRIP_BY_DRIVER.split("?").first())
            && method.equals("GET")) {
            msg = "mock_api_trips_by_driver_response.json"
        } else {
            throw IllegalArgumentException("Unsupported API method: " + url + " " + method)
        }
        return msg
    }

    private fun matchPath(pathSegments: List<String>, urlPattern: String): Boolean {
        var patternTokens = urlPattern.split("/")
        patternTokens = patternTokens.subList(1, patternTokens.size)
        return pathSegments.intersect(patternTokens.toSet()).size == patternTokens.size
    }

    private fun matchQuery(queryParameterNames: MutableSet<String>, urlPattern: String): Boolean {
        val queryPairs = urlPattern.split("&")
        val patternQueryParameterNames = mutableListOf<String>()
        for (pair in queryPairs) {
            patternQueryParameterNames.add(pair.split("=").first())
        }
        return queryParameterNames.intersect(patternQueryParameterNames.toSet()).size == patternQueryParameterNames.size
    }
}