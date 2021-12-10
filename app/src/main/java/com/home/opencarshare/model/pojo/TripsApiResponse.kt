package com.home.opencarshare.model.pojo

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.util.*


class TripsApiResponse {

    @SerializedName("code")
    @Expose
    var code: String = ""

    @SerializedName("message")
    @Expose
    var message: String = ""

    @SerializedName("result")
    @Expose
    var result: List<Trip> = Collections.emptyList()

}
