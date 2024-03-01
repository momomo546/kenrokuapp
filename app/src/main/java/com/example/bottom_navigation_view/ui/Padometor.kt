package com.example.bottom_navigation_view.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Padometor : SensorEventListener {
    private var steps = 0
    private val PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 1001
    private var mSensorManager: SensorManager? = null
    private var mStepDetectorSensor: Sensor? = null
    private var mStepConterSensor: Sensor? = null

    fun padometorPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED) {
            // パーミッションが許可されていないのでリクエストする
            ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                PERMISSION_REQUEST_ACTIVITY_RECOGNITION)
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}