package com.home.opencarshare.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Driver {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("tripsCount")
    @Expose
    var tripsCount: String? = null

    @SerializedName("cell")
    @Expose
    var cell: String? = null
}