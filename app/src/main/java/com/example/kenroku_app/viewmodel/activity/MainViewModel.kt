package com.example.kenroku_app.viewmodel.activity

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kenroku_app.R
import com.example.kenroku_app.model.services.gps.GPSManager
import com.example.kenroku_app.model.services.gps.actions.CheckPointFlagCheck
import com.example.kenroku_app.model.services.gps.actions.SeasonFlagCheck
import com.example.kenroku_app.model.services.gps.actions.VisitCount
import com.example.kenroku_app.model.services.step_counter.StepCounter

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private lateinit var gpsManager: GPSManager
    private lateinit var stepCounter: StepCounter
    private lateinit var visitCount: VisitCount
    private lateinit var checkPointFlagCheck: CheckPointFlagCheck

    private val _message = MutableLiveData<String>()
    private val _steps = MutableLiveData<Int>()
    val steps: LiveData<Int> get() = _steps
    val message: LiveData<String> get() = _message
    var isLocation = false
    private var onPopUpCallback: ((String) -> Unit)? = null

    fun initializeServices() {
        visitCount = VisitCount(context)
        checkPointFlagCheck = CheckPointFlagCheck(context)
        gpsManager = GPSManager(context,
            { location -> onLocation(location) }
        )
        stepCounter = StepCounter(
            context,
            this,
            onStepDetected = { stepCount -> _steps.postValue(stepCount) }
        )
    }

    fun setOnPopUpCallback(callback: (String) -> Unit) {
        onPopUpCallback = callback
    }

    fun playNotificationSound() {
        val mediaPlayer = MediaPlayer.create(context, R.raw.rappa)
        mediaPlayer.start()
    }

    fun updateVisitCount() {
        visitCount.add()
    }

    fun checkPointCheck() {
        val seasonFlagCheck = SeasonFlagCheck(context) { message ->
            // 位置情報の更新があったときの処理
            onPopUpCallback?.invoke(message)
        }
        seasonFlagCheck.checkSeasonFlag()
    }

    private fun requestStepCounterPermission() {
        // Activityにパーミッションリクエストを伝える処理
        // 必要であればUI層にメッセージを送る
    }

    private fun onLocation(location: Boolean) {
        // 位置情報が変わったときの処理
        isLocation=location
        if(!isLocation) return
        updateVisitCount()
        checkPointCheck()
    }


    fun registerStepCounterListener() {
        stepCounter.registerStepCounterListener()
    }

    fun unregisterStepCounterListener() {
        stepCounter.unregisterStepCounterListener()
    }

    fun startLocationUpdates() {
        gpsManager.registerGpsUpdates()
    }

    fun stopLocationUpdates() {
        gpsManager.unregisterGpsUpdates()
    }
}
