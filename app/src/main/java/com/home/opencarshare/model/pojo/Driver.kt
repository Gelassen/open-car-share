package com.home.opencarshare.model.pojo

import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Driver(
    @SerializedName("id")
    @Expose
    var id: String = "",
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("tripsCount")
    @Expose
    var tripsCount: String = "",
    @SerializedName("cell")
    @Expose
    var cell: String = ""
): Parcelable {

    fun isEmpty(): Boolean {
        return id.isEmpty()
                || name.isEmpty()
                || tripsCount.isEmpty()
                || cell.isEmpty()
    }

    fun from(driver: Driver) {
        this.id = driver.id
        this.name = driver.name
        this.tripsCount = driver.tripsCount
        this.cell = driver.cell
    }
}