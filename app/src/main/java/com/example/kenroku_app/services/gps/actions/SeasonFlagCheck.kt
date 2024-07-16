package com.example.kenroku_app.services.gps.actions

import android.content.Context
import android.util.Log
import com.example.kenroku_app.repositories.models.MarkerData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class SeasonFlagCheck(private val context: Context, private val callback: (String) -> Unit) {
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
        MarkerData.seasonFlag = seasonFlag
    }

    fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH) + 1  // 月は0から始まるため、+1する
    }

    fun checkSeasonFlag() {
        val month = getCurrentMonth()
        if(3<=month&&month<=5) {
            if(seasonFlag[0]==false){
                seasonFlag[0] = true
                callback("春バッジを獲得しました。")
            }
        }
        else if(6<=month&&month<=8) {
            if(seasonFlag[1]==false){
                seasonFlag[1] = true
                callback("夏バッジを獲得しました。")
            }
        }
        else if(9<=month&&month<=11) {
            if(seasonFlag[2]==false){
                seasonFlag[2] = true
                callback("秋バッジを獲得しました。")
            }
        }
        else {
            if(seasonFlag[3]==false){
                seasonFlag[3] = true
                callback("冬バッジを獲得しました。")
            }
        }

        MarkerData.seasonFlag = seasonFlag
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