package com.example.bottom_navigation_view.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bottom_navigation_view.CheckPointFlagCheck
import com.example.bottom_navigation_view.LocationCheck
import com.example.bottom_navigation_view.MainActivity
import com.example.bottom_navigation_view.ui.dashboard.BadgeFlag

class GPSManager(private val context: Context, private  val activity: MainActivity, private val callback: () -> Unit) {
    var isLocation = false
    val visitCount = VisitCount(context)
    val checkPointFlagCheck = CheckPointFlagCheck(context)

    private val locationManager: LocationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // 位置情報が変更されたときの処理
            val locationCheck = LocationCheck()
            isLocation = locationCheck.isWithinRange(location)
            activity.isLocation = isLocation
            if(isLocation) {
                visitCount.add()
                callback()
            }
            for(i in BadgeFlag.markerPosition.indices) {
                val targetLocation = Location("target")
                targetLocation.latitude = BadgeFlag.markerPosition[i].latitude
                targetLocation.longitude = BadgeFlag.markerPosition[i].longitude
                val distance = location.distanceTo(targetLocation)

                checkPointFlagCheck.checkCheckPointFlag(i,distance, BadgeFlag.kenrokuenMarker)

                //Log.d("debug", distance.toString())
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

    fun startGpsUpdates() {
        // GPSの更新を開始する
        Log.d("debug", "locationStart()")

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("debug", "location manager Enabled")
        } else {
            // to prompt setting up GPS
            /*val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(settingsIntent)
            Log.d("debug", "not gpsEnable, startActivity")*/
        }

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)

            Log.d("debug", "checkSelfPermission false")
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            3000,
            0f,
            locationListener)
    }

    fun stopGpsUpdates() {
        // GPSの更新を停止する
        locationManager.removeUpdates(locationListener)
    }
}
