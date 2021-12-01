package com.home.opencarshare.network

import android.content.Context
import android.net.Uri
import okhttp3.*
import java.lang.IllegalArgumentException

class MockInterceptor(val context: Context): Interceptor {

    companion object {

        private const val NO_QUERY = ""

        private const val URL_TRIP = "/api/trips?city=someCity&time=10000000"

        private const val URL_TRIP_BY_ID = "/api/trips?id=101"

        private val URL_MODELS = "/api/Models"

        private val URL_SPARES = "/api/spares"

        private val URL_SCHEDULES = "/api/ScheduleRequests"

        private val URL_APPLICATION = "/api/application"

        private val URL_CALENDAR = "/api/calendar"

        private val URL_SERVICES = "/api/services"
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
        } else if(url.contains(URL_TRIP_BY_ID) && method.equals("POST")) {
            msg = "mock_carmake_response.json"
        } else if (url.contains(URL_MODELS) && method.equals("GET")) {
            msg = "mock_get_models_response.json"
        } else if (url.contains(URL_MODELS) && method.equals("POST")) {
            msg = "mock_model_response.json"
        } else if (url.contains(URL_SPARES)) {
            msg = "mock_spares_full.json"
        } else if (url.contains(URL_SCHEDULES)) {
            msg = "mock_schedules_full.json"
        } else if (url.contains(URL_APPLICATION)) {
            msg = "mock_application.json"
        } else if (url.contains(URL_CALENDAR)) {
            msg = "mock_calendar.json"
        } else if (url.contains(URL_SERVICES)) {
            msg = "mock_services_full.json"
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