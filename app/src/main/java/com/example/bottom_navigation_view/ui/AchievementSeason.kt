package com.example.bottom_navigation_view.ui

import com.example.bottom_navigation_view.ui.dashboard.BadgeFlag
import com.example.bottom_navigation_view.ui.dashboard.MyData

class AchievementSeason(private val myData: List<MyData>,private val position:Int) : Achievement(myData) {
    val item = myData[position]
    override fun checkAchievementConditions(){
        if (BadgeFlag.seasonFlag[position]) {
            imageresource = item.imageResId
            text = item.text
        }
    }
}

class AchievementCheckPoint(private val myData: List<MyData>,private val position:Int) : Achievement(myData) {
    val item = myData[position]
    override fun checkAchievementConditions(){
        if (BadgeFlag.checkPointFlag[position]) {
            imageresource = item.imageResId
            text = item.text
        }
    }
}