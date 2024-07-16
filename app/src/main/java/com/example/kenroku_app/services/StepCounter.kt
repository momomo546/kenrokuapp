package com.example.kenroku_app.services

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.kenroku_app.activities.MainActivity
import com.example.kenroku_app.repositories.models.MarkerData

class StepCounter(context: Context, private  val activity: MainActivity) : SensorEventListener {

    private val PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 1001
    private var mSensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var mStepDetectorSensor: Sensor? = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    private var mStepConterSensor: Sensor? = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    val sharedPreferences = context.getSharedPreferences("padometor", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    private var steps = sharedPreferences.getInt("step", 0)

    init {
        if (mStepDetectorSensor == null) {
            Log.e("StepCounter", "Step Detector Sensor is not available!")
        }
        if (mStepConterSensor == null) {
            Log.e("StepCounter", "Step Counter Sensor is not available!")
        }
        stepCounterPermission(activity)
    }

    fun stepCounterPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED) {
            // パーミッションが許可されていないのでリクエストする
            ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                PERMISSION_REQUEST_ACTIVITY_RECOGNITION)
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if(!activity.isLocation) return
        val sensor = event.sensor
        val values = event.values
        //TYPE_STEP_COUNTER
        if (sensor.type == Sensor.TYPE_STEP_COUNTER) {
            // sensor からの値を取得するなどの処理を行う
            Log.d("type_step_counter", values[0].toString())
        }
        steps++
        MarkerData.steps = steps
        editor.putInt("step", steps)
        editor.apply()
    }

    fun registerStepCounterListener() {
        if (mSensorManager == null) {
            println("Sensor Manager is not available!")
        } else {
            mStepConterSensor?.let {
                mSensorManager.registerListener(
                    this,
                    it,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            }
            mStepDetectorSensor?.let {
                mSensorManager.registerListener(
                    this,
                    it,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            }
        }
    }

    fun unregisterStepCounterListener(){
        mStepConterSensor?.let {
            mSensorManager.unregisterListener(this, it)
        }
        mStepDetectorSensor?.let {
            mSensorManager.unregisterListener(this, it)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}