package com.home.opencarshare.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class Trip {
    @SerializedName("id")
    @Expose
    var id: String = ""

    @SerializedName("locationFrom")
    @Expose
    var locationFrom: String = ""

    @SerializedName("locationTo")
    @Expose
    var locationTo: String = ""

    @SerializedName("date")
    @Expose
    var date: String = "Wed, 4 Jul 2001 12:08:56 -0700"

    @SerializedName("availableSeats")
    @Expose
    var availableSeats: String = ""

    @SerializedName("driver")
    @Expose
    lateinit var driver: Driver
}