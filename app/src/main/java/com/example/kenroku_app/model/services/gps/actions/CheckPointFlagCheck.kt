package com.example.kenroku_app.model.services.gps.actions

import android.content.Context
import com.example.kenroku_app.R
import com.example.kenroku_app.model.repositories.data.AchieveData
import com.example.kenroku_app.model.services.google_map.KenrokuenMarker
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CheckPointFlagCheck(context: Context) {
    // 変数保存
    private val sharedPreferences = context.getSharedPreferences("checkPointFlag", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()
    private val gson = Gson()

    private val AREA = 15
    private var checkPointFlag = MutableList(27) { false }

    init {
        val jsonFlag = sharedPreferences.getString("checkPointFlag", "")
        checkPointFlag = if (jsonFlag == ""){
            MutableList(27){ false }
        } else {
            val type = object : TypeToken<MutableList<Boolean>>() {}.type
            gson.fromJson(jsonFlag, type) ?: mutableListOf()
        }
        AchieveData.checkPointFlag = checkPointFlag
    }

    fun checkCheckPointFlag(index: Int,distance: Float,kenrokuenMarker: KenrokuenMarker?) {
        if(distance<AREA) {
            if (kenrokuenMarker != null && !AchieveData.checkPointFlag[index]) {
                kenrokuenMarker.removeMarker(index)
                kenrokuenMarker.markerList[index].icon(BitmapDescriptorFactory.fromResource(R.drawable.check_mark))
                kenrokuenMarker.resetMarker(index)
            }
            checkPointFlag[index] = true
            AchieveData.checkPointFlag = checkPointFlag

            val json = gson.toJson(checkPointFlag)
            editor.putString("checkPointFlag", json)
            editor.apply()
        }
    }
}