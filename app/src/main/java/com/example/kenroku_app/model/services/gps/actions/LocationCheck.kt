package com.example.kenroku_app.model.services.gps.actions

import android.location.Location

class LocationCheck {
    fun isWithinRange(userLocation: Location): Boolean {
        val targetLocation = Location("target")
        val radius = 262

        targetLocation.latitude = 36.5624129
        targetLocation.longitude = 136.662417

        val distance = userLocation.distanceTo(targetLocation)

        return distance <= radius
    }
}