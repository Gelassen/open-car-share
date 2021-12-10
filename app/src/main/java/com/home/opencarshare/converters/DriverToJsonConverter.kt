package com.home.opencarshare.converters

import com.google.gson.Gson
import com.home.opencarshare.model.pojo.Driver

class DriverToJsonConverter {

    fun convertDriverToJson(driver: Driver): String {
        return Gson().toJson(driver)
    }

    fun convertJsonToDriver(json: String?): Driver {
        if (json == null) return Driver()
        return Gson().fromJson(json, Driver::class.java)
    }
}