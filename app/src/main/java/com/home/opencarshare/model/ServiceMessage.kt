package com.home.opencarshare.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ServiceMessage() {

    constructor(status: String) : this(status, "") {
        // no op
    }

    constructor(status: String, message: String) : this() {
        this.status = status
        this.message = message
    }

    object Status {
        const val SUCCEED = "1"
        const val FAILED = "0"
        const val NONE = "-1"
    }

    @SerializedName("status")
    @Expose
    var status: String = Status.SUCCEED

    @SerializedName("message")
    @Expose
    var message: String = ""
}