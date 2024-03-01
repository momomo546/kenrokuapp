package com.example.bottom_navigation_view

import android.content.Context
import com.example.bottom_navigation_view.ui.dashboard.BadgeFlag
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CheckPointFlagCheck(private val context: Context) {
    // 変数保存
    val sharedPreferences = context.getSharedPreferences("checkPointFlag", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    private val gson = Gson()

    var AREA = 15
    var checkPointFlag = MutableList(27) { false }

    init {
        val jsonFlag = sharedPreferences.getString("checkPointFlag", "")
        if (jsonFlag == ""){
            checkPointFlag = MutableList(27){ false }
        }
        else {
            val type = object : TypeToken<MutableList<Boolean>>() {}.type
            checkPointFlag = gson.fromJson(jsonFlag, type) ?: mutableListOf()
        }
        BadgeFlag.checkPointFlag = checkPointFlag
    }

    fun checkCheckPointFlag(index: Int,distance: Float) {
        if(distance<AREA) {
            checkPointFlag[index] = true

            BadgeFlag.checkPointFlag = checkPointFlag

            val json = gson.toJson(checkPointFlag)
            editor.putString("checkPointFlag", json)
            editor.apply()
        }
    }
}