package com.example.kenroku_app.model.services.gps.actions

import android.content.Context
import android.util.Log
import com.example.kenroku_app.model.repositories.data.AchieveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class SeasonFlagCheck(private val context: Context, private val callback: (String) -> Unit) {
    // 変数保存
    private val sharedPreferences = context.getSharedPreferences("seasonFlag", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()
    private val gson = Gson()

    private var seasonFlag = MutableList(4){ false }

    init {
        val jsonFlag = sharedPreferences.getString("seasonFlag", "")
        seasonFlag = if (jsonFlag == "") {
            MutableList(4) { false }
        } else {
            val type = object : TypeToken<MutableList<Boolean>>() {}.type
            gson.fromJson(jsonFlag, type) ?: mutableListOf()
        }
        AchieveData.seasonFlag = seasonFlag
    }

    private fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH) + 1  // 月は0から始まるため、+1する
    }

    fun checkSeasonFlag() {
        val month = getCurrentMonth()
        if(month in 3..5) {
            if(!seasonFlag[0]){
                seasonFlag[0] = true
                callback("春バッジを獲得しました。")
            }
        }
        else if(month in 6..8) {
            if(!seasonFlag[1]){
                seasonFlag[1] = true
                callback("夏バッジを獲得しました。")
            }
        }
        else if(month in 9..11) {
            if(!seasonFlag[2]){
                seasonFlag[2] = true
                callback("秋バッジを獲得しました。")
            }
        }
        else {
            if(!seasonFlag[3]){
                seasonFlag[3] = true
                callback("冬バッジを獲得しました。")
            }
        }

        AchieveData.seasonFlag = seasonFlag
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