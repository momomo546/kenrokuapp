package com.example.kenroku_app.activities

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kenroku_app.CheckPointFlagCheck
import com.example.kenroku_app.R
import com.example.kenroku_app.SeasonFlagCheck
import com.example.kenroku_app.databinding.ActivityMainBinding
import com.example.kenroku_app.services.GPSManager
import com.example.kenroku_app.services.StepCounter
import com.example.kenroku_app.utils.VisitCount
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    var isLocation = false
    lateinit var visitCount: VisitCount
    lateinit var checkPointFlagCheck: CheckPointFlagCheck

    lateinit var gpsManager: GPSManager
    lateinit var stepCounter: StepCounter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        visitCount = VisitCount(this)
        checkPointFlagCheck = CheckPointFlagCheck(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_achieve, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()

        gpsManager =
            GPSManager(this, this) { -> checkPointCheck() }
        stepCounter = StepCounter(this,this)
    }

    override fun onResume() {
        super.onResume()
        stepCounter.registerStepCounterListener()
    }

    override fun onPause() {
        super.onPause()
        stepCounter.unregisterStepCounterListener()
    }

    private fun checkPointCheck() {
        val seasonFlagCheck = SeasonFlagCheck(this) { message ->
            // 位置情報の更新があったときの処理
            popUp(message)
        }
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