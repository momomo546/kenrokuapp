package com.example.bottom_navigation_view

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bottom_navigation_view.databinding.ActivityMainBinding
import com.example.bottom_navigation_view.ui.GPSManager
import com.example.bottom_navigation_view.ui.StepCounter
import com.example.bottom_navigation_view.ui.VisitCount
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    private val PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 1001
    var isLocation = false
    lateinit var visitCount: VisitCount
    lateinit var checkPointFlagCheck: CheckPointFlagCheck

    lateinit var gpsManager: GPSManager
    lateinit var stepCounter: StepCounter

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
    { isGranted: Boolean ->
        if (isGranted) {
            // 使用が許可された
            gpsManager.startGpsUpdates()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        visitCount = VisitCount(this)
        checkPointFlagCheck = CheckPointFlagCheck(this)

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
        CoroutineScope(Dispatchers.Main).launch {
            // 非同期処理を開始
            async { gpsPermission() }.await()
            async { stepCounterPermission() }.await()

            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }
    }

    override fun onStart() {
        super.onStart()

        gpsManager = GPSManager(this,this) { -> checkPointCheck() }
        stepCounter = StepCounter(this,this)
    }

    private fun stepCounterPermission() {
        //hosuukei
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED) {
            // パーミッションが許可されていないのでリクエストする
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                PERMISSION_REQUEST_ACTIVITY_RECOGNITION)
        }
        //hosuukei
    }

    private fun gpsPermission() {
        //gps
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            gpsManager.startGpsUpdates()
        }
        //gps
    }

    //hosuukei
    override fun onResume() {
        super.onResume()
        stepCounter.registerStepCounterListener()
    }

    override fun onPause() {
        super.onPause()
        stepCounter.unregisterStepCounterListener()
    }
    //hosuukei

    private fun checkPointCheck() {
        val seasonFlagCheck = SeasonFlagCheck(this){message ->
            // 位置情報の更新があったときの処理
            popUp(message)}
        seasonFlagCheck.checkSeasonFlag()
    }
    private fun popUp(message: String){
        val snackbar = Snackbar.make(findViewById(R.id.nav_host_fragment_activity_main), message, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("閉じる") {
            snackbar.dismiss()
        }
        snackbar.show()
        val mediaPlayer = MediaPlayer.create(this, R.raw.rappa)
        mediaPlayer.start()
    }
}