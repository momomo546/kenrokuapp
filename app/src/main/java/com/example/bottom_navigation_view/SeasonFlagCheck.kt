package com.example.bottom_navigation_view

import android.content.Context
import android.util.Log
import com.example.bottom_navigation_view.ui.dashboard.BadgeFlag
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class SeasonFlagCheck(private val context: Context) {
    // 変数保存
    val sharedPreferences = context.getSharedPreferences("seasonFlag", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    private val gson = Gson()

    var seasonFlag = MutableList(4){ false }

    init {
        val jsonFlag = sharedPreferences.getString("seasonFlag", "")
        if (jsonFlag == "") {
            seasonFlag = MutableList(4) { false }
        }
        else {
            val type = object : TypeToken<MutableList<Boolean>>() {}.type
            seasonFlag = gson.fromJson(jsonFlag, type) ?: mutableListOf()
        }
        BadgeFlag.seasonFlag = seasonFlag
    }

    fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH) + 1  // 月は0から始まるため、+1する
    }

    fun checkSeasonFlag() {
        val month = getCurrentMonth()
        if(3<=month&&month<=5) seasonFlag[0] = true
        else if(6<=month&&month<=8) seasonFlag[1] = true
        else if(9<=month&&month<=11) seasonFlag[2] = true
        else seasonFlag[3] = true

        BadgeFlag.seasonFlag = seasonFlag
        val json = gson.toJson(seasonFlag)
        editor.putString("seasonFlag", json)
        editor.apply()
    }

    fun check() {
        Log.d("debug", seasonFlag[0].toString())
        Log.d("debug", seasonFlag[1].toString())
        Log.d("debug", seasonFlag[2].toString())
        Log.d("debug", seasonFlag[3].toString())
    }
}