package com.example.kenroku_app.fragments.achieve

import com.example.kenroku_app.fragments.MarkerData

class AchievementSeason(private val myData: List<MyData>,private val position:Int) : Achievement(myData) {
    val item = myData[position]
    override fun checkAchievementConditions(){
        if (MarkerData.seasonFlag[position]) {
            imageresource = item.imageResId
            text = item.text
        }
    }
}

class AchievementCheckPoint(private val myData: List<MyData>,private val position:Int) : Achievement(myData) {
    val item = myData[position]
    override fun checkAchievementConditions(){
        if (MarkerData.checkPointFlag[position]) {
            imageresource = item.imageResId
            text = item.text
        }
    }
}