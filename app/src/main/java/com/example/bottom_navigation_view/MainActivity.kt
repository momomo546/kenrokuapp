package com.example.bottom_navigation_view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bottom_navigation_view.databinding.ActivityMainBinding
import com.example.bottom_navigation_view.ui.VisitCount
import com.example.bottom_navigation_view.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() , LocationListener , SensorEventListener {

    private lateinit var binding: ActivityMainBinding
    private var locationManager: LocationManager? = null

    private var mSensorManager: SensorManager? = null
    private var mStepDetectorSensor: Sensor? = null
    private var mStepConterSensor: Sensor? = null
    private val PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 1001
    var steps = 0
    private var isLocation = false
    lateinit var visitCount: VisitCount
    lateinit var checkPointFlagCheck: CheckPointFlagCheck

    // 変数保存
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: Editor


    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
    { isGranted: Boolean ->
        if (isGranted) {
            // 使用が許可された
            locationStart()
        }
        else {
            // それでも拒否された時の対応
            val toast = Toast.makeText(this, "これ以上なにもできません", Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        visitCount = VisitCount(this)
        checkPointFlagCheck = CheckPointFlagCheck(this)

        sharedPreferences = getSharedPreferences("padometor", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        //seasoncheck
        val seasonFlagCheck = SeasonFlagCheck(this)
        seasonFlagCheck.checkSeasonFlag()
        seasonFlagCheck.check()
        //seasoncheck

        // step呼び出し
        steps = sharedPreferences.getInt("step", 0)

        //gps
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            locationStart()
        }
        //gps

        //hosuukei
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED) {
            // パーミッションが許可されていないのでリクエストする
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                PERMISSION_REQUEST_ACTIVITY_RECOGNITION)
        }

        //センサーマネージャを取得
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        //センサマネージャから TYPE_STEP_DETECTOR についての情報を取得する
        mStepDetectorSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        //センサマネージャから TYPE_STEP_COUNTER についての情報を取得する
        mStepConterSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        //hosuukei

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    //gps
    private fun locationStart() {
        Log.d("debug", "locationStart()")

        // Instances of LocationManager class must be obtained using Context.getSystemService(Class)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("debug", "location manager Enabled")
        } else {
            // to prompt setting up GPS
            val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(settingsIntent)
            Log.d("debug", "not gpsEnable, startActivity")
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)

            Log.d("debug", "checkSelfPermission false")
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            3000,
            0f,
            this)
    }

    override fun onLocationChanged(location: Location) {
        val locationCheck = LocationCheck()
        isLocation = locationCheck.isWithinRange(location)
        if(isLocation) visitCount.add()
        for(i in HomeFragment.markerPosition.indices) {
            val targetLocation = Location("target")
            targetLocation.latitude = HomeFragment.markerPosition[i].latitude
            targetLocation.longitude = HomeFragment.markerPosition[i].longitude
            val distance = location.distanceTo(targetLocation)

            checkPointFlagCheck.checkCheckPointFlag(i,distance)

            //Log.d("debug", distance.toString())
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?){}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
    //gps

    //hosuukei
    override fun onSensorChanged(event: SensorEvent) {
        if(!isLocation) return
        val sensor = event.sensor
        val values = event.values
        //TYPE_STEP_COUNTER
        if (sensor.type == Sensor.TYPE_STEP_COUNTER) {
            // sensor からの値を取得するなどの処理を行う
            Log.d("type_step_counter", values[0].toString())
        }
        steps++
        editor.putInt("step", steps)
        editor.apply()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onResume() {
        super.onResume()
        mSensorManager!!.registerListener(
            this,
            mStepConterSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        mSensorManager!!.registerListener(
            this,
            mStepDetectorSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        mSensorManager!!.unregisterListener(this, mStepConterSensor)
        mSensorManager!!.unregisterListener(this, mStepDetectorSensor)
    }
    //hosuukei
}