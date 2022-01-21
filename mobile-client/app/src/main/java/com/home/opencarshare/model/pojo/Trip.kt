package com.home.opencarshare.model.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
class Trip(
    @SerializedName("id")
    @Expose
    var id: String = "",
    @SerializedName("locationFrom")
    @Expose
    var locationFrom: String = "",
    @SerializedName("locationTo")
    @Expose
    var locationTo: String = "",
    @SerializedName("date")
    @Expose
    var date: Long = 0L,
    @SerializedName("availableSeats")
    @Expose
    var availableSeats: String = "1",
    @SerializedName("driverId")
    @Expose
    var driverId: String = "",
    @SerializedName("driver")
    @Expose
    var driver: Driver = Driver()
) : Parcelable {

    fun isEmpty(): Boolean {
        return id.isEmpty()
                || locationFrom.isEmpty()
//                || date.isEmpty()
                || availableSeats.isEmpty()
//                || driver.isEmpty()
    }

    override fun toString(): String {
        return "Trip(id='$id', locationFrom='$locationFrom', locationTo='$locationTo', date='$date', availableSeats='$availableSeats')"
    }


}