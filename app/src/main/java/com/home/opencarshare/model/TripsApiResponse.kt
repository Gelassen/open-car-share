package com.home.opencarshare.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.util.*


class TripsApiResponse {

    @SerializedName("code")
    @Expose
    var code: String = ""

    @SerializedName("result")
    @Expose
    var result: List<Trip> = Collections.emptyList()

}
