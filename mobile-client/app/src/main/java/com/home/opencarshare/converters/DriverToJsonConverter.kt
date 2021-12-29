package com.home.opencarshare.converters

import com.google.gson.Gson
import com.home.opencarshare.model.pojo.Driver
import com.home.opencarshare.model.pojo.DriverCredentials

class DriverToJsonConverter {

    fun convertDriverToJson(driver: DriverCredentials): String {
        val result = Gson().toJson(driver)
        return result
    }

    fun convertJsonToDriver(json: String?): DriverCredentials {
        if (json == null) return DriverCredentials()
        val result = Gson().fromJson(json, DriverCredentials::class.java)
        return result
    }
}