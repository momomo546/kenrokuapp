package com.example.kenroku_app.view.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kenroku_app.R
import com.example.kenroku_app.databinding.ActivityMainBinding
import com.example.kenroku_app.viewmodel.activity.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private val PERMISSION_REQUEST_FINE_LOCATION = 1001
    private val PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // コールバックの設定
        viewModel.setOnPopUpCallback { message ->
            popUp(message)
        }

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_achieve, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        requestPermissions()
    }

    override fun onStart() {
        super.onStart()
        viewModel.initializeServices()
    }

    override fun onResume() {
        super.onResume()
        viewModel.registerStepCounterListener()
        viewModel.startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        viewModel.unregisterStepCounterListener()
        viewModel.stopLocationUpdates()
    }

    private fun requestPermissions() {
        val permissions = mutableListOf<String>()

        // 位置情報のパーミッションチェック
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        // 歩数計のパーミッションチェック
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACTIVITY_RECOGNITION)
        }
        // パーミッションが未許可の場合にリクエスト
        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this,
                permissions.toTypedArray(),
                PERMISSION_REQUEST_FINE_LOCATION)
        } else {
            // すべてのパーミッションが許可されている場合の処理
            onPermissionsGranted()
        }
    }
    private fun onPermissionsGranted() {
        viewModel.initializeServices()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 位置情報のパーミッションが許可された場合の処理
                } else {
                    // 位置情報のパーミッションが拒否された場合の処理
                    popUp2("必要なパーミッションが拒否されました。アプリの全機能を利用するには、パーミッションを許可してください。")
                }
            }

            PERMISSION_REQUEST_ACTIVITY_RECOGNITION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 歩数計のパーミッションが許可された場合の処理
                } else {
                    // 歩数計のパーミッションが拒否された場合の処理
                    popUp2("必要なパーミッションが拒否されました。アプリの全機能を利用するには、パーミッションを許可してください。")
                }
            }
        }
    }

    private fun popUp(message: String){
        val snackbar = Snackbar.make(findViewById(R.id.nav_host_fragment_activity_main), message, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("閉じる") {
            snackbar.dismiss()
        }
        snackbar.show()
        viewModel.playNotificationSound()
    }
    private fun popUp2(message: String) {
        val snackbar = Snackbar.make(
            findViewById(R.id.nav_host_fragment_activity_main),
            message,
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction("閉じる") {
            snackbar.dismiss()
        }
    }
}