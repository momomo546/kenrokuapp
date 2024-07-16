package com.example.kenroku_app.services.gps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.kenroku_app.activities.MainActivity
import com.example.kenroku_app.repositories.models.MarkerData
import com.example.kenroku_app.services.gps.actions.CheckPointFlagCheck
import com.example.kenroku_app.services.gps.actions.LocationCheck
import com.example.kenroku_app.services.gps.actions.VisitCount

class GPSManager(private val context: Context, private  val activity: MainActivity, private val callback: () -> Unit) {
    var isLocation = false
    val visitCount = VisitCount(context)
    val checkPointFlagCheck = CheckPointFlagCheck(context)
    private val minTimeGpsCheck : Long = 1000
    val minDistanceGpsCheck = 0f

    private val locationManager: LocationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    private val requestPermissionLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestPermission())
    { isGranted: Boolean ->
        if (isGranted) {
            // 使用が許可された
            startGpsUpdates()
        }
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
            for(i in MarkerData.markerPosition.indices) {
                val targetLocation = Location("target")
                targetLocation.latitude = MarkerData.markerPosition[i].latitude
                targetLocation.longitude = MarkerData.markerPosition[i].longitude
                val distance = location.distanceTo(targetLocation)

                checkPointFlagCheck.checkCheckPointFlag(i,distance,
                    MarkerData.kenrokuenMarker
                )

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
    init {
        gpsPermission()
    }

    private fun gpsPermission() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            println("GPS available")
            startGpsUpdates()
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
            minTimeGpsCheck,
            minDistanceGpsCheck,
            locationListener)
    }

    fun stopGpsUpdates() {
        // GPSの更新を停止する
        locationManager.removeUpdates(locationListener)
    }
}
