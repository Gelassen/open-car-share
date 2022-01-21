package com.home.opencarshare.model.pojo

import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
open class Driver(
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
    var cell: String = "",
    @SerializedName("trips")
    @Expose
    var trips: List<Trip> = emptyList()
): Parcelable {

    fun isEmpty(): Boolean {
        return /*id.isEmpty()
                || tripsCount.isEmpty()
                ||*/ name.isEmpty()
                || cell.isEmpty()
    }

    open fun from(driver: Driver) {
        this.id = driver.id
        this.name = driver.name
        this.tripsCount = driver.tripsCount
        this.cell = driver.cell
    }

    override fun toString(): String {
        return "Driver(id='$id', name='$name', tripsCount='$tripsCount', cell='$cell')"
    }
}

@Parcelize
class DriverCredentials(
        @SerializedName("id")
        var id: String = "",
        @SerializedName("name")
        @Expose
        var name: String = "",
        @SerializedName("tripsCount")
        @Expose
        var tripsCount: String = "0",
        @SerializedName("cell")
        @Expose
        var cell: String = "",
        @SerializedName("secret")
        @Expose
        var secret: String = "",
        @SerializedName("trips")
        @Expose
        var trips: List<Trip> = emptyList()
        ) : Parcelable {

    fun from(driver: DriverCredentials) {
        this.id = driver.id
        this.name = driver.name
        this.tripsCount = driver.tripsCount
        this.cell = driver.cell
        this.secret = secret // TODO check it - is it a bug or by intent?
    }

    fun isEmpty(): Boolean {
        return name.isEmpty()
                || cell.isEmpty()
    }

    fun toDriver(): Driver {
        return Driver(
            id = id,
            name = name,
            tripsCount = tripsCount,
            cell = cell,
        )
    }

    override fun toString(): String {
        return "DriverCredentials(id='$id', " +
                "name='$name', " +
                "tripsCount='$tripsCount', " +
                "cell='$cell', " +
                "secret='$secret')"
    }


}