package io.github.kabirnayeem99.pocketmonster

import android.location.Location

data class PockeMon(
    var name: String,
    var description: String,
    var imageResource: Int,
    var power: Double,
    var latitude: Double,
    var longtitude: Double,
    var isCatch: Boolean? = false
) {
    public var location = Location(name)

    init {
        location.latitude = this.latitude
        location.longitude = this.longtitude
    }
}