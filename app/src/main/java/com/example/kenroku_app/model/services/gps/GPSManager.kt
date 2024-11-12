package com.example.kenroku_app.model.services.gps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.kenroku_app.model.repositories.data.MarkerData
import com.example.kenroku_app.model.services.gps.actions.CheckPointFlagCheck
import com.example.kenroku_app.model.services.gps.actions.LocationCheck
import com.example.kenroku_app.model.services.gps.actions.VisitCount

class GPSManager(
    private val context: Context,
    private val onLocation: (Boolean) -> Unit
) {
    var isLocation = false
    val visitCount = VisitCount(context)
    val checkPointFlagCheck = CheckPointFlagCheck(context)
    private val minTimeGpsCheck : Long = 1000
    private val minDistanceGpsCheck = 0f

    private val locationManager: LocationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // 位置情報が変更されたときの処理
            val locationCheck = LocationCheck()
            isLocation = locationCheck.isWithinRange(location)
            if(isLocation) {
                visitCount.add()
                onLocation(isLocation)
            }
            for(i in MarkerData.yamanakaMarkerPosition.indices) {
                val targetLocation = Location("target")
                targetLocation.latitude = MarkerData.yamanakaMarkerPosition[i].latitude
                targetLocation.longitude = MarkerData.yamanakaMarkerPosition[i].longitude
                val distance = location.distanceTo(targetLocation)

                checkPointFlagCheck.checkCheckPointFlag(i,distance, MarkerData.googleMapMarker)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            // プロバイダの状態が変更されたときの処理
        }

        override fun onProviderEnabled(provider: String) {
            // プロバイダが有効になったときの処理
        }

        override fun onProviderDisabled(provider: String) {
            // プロバイダが無効になったときの処理
        }
    }

    fun registerGpsUpdates() {
        // GPSの更新を開始する
        Log.d("debug", "locationStart()")

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("debug", "location manager Enabled")
        }

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            minTimeGpsCheck,
            minDistanceGpsCheck,
            locationListener)
    }

    fun unregisterGpsUpdates() {
        // GPSの更新を停止する
        locationManager.removeUpdates(locationListener)
    }
}
