package com.example.kenroku_app

import android.location.Location

class LocationCheck {
    fun isWithinRange(userLocation: Location): Boolean {
        val targetLocation = Location("target")
        //var istrue = false
        val radius = 262
        targetLocation.latitude = 36.5624129
        targetLocation.longitude = 136.662417

        val distance = userLocation.distanceTo(targetLocation)
        /*if(distance <= radius) istrue = true
        Log.d("debug", istrue.toString())*/

        return distance <= radius
    }
}