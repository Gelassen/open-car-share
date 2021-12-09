package com.home.opencarshare.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TripBookingApiResponse {

    @SerializedName("code")
    @Expose
    var code: String = ""

    @SerializedName("message")
    @Expose
    var message: String = ""

    @SerializedName("result")
    @Expose
    var result: ServiceMessage = ServiceMessage()
}