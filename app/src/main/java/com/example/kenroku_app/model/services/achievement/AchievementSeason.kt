package com.example.kenroku_app.model.services.achievement

import com.example.kenroku_app.model.repositories.data.MarkerData
import com.example.kenroku_app.view.fragments.achieve.badge.BadgeData

class AchievementSeason(private val badgeData: List<BadgeData>, private val position:Int) : Achievement(badgeData) {
    val item = badgeData[position]
    override fun checkAchievementConditions(){
        if (MarkerData.seasonFlag[position]) {
            imageresource = item.imageResId
            text = item.textId
        }
    }
}

class AchievementCheckPoint(private val badgeData: List<BadgeData>, private val position:Int) : Achievement(badgeData) {
    val item = badgeData[position]
    override fun checkAchievementConditions(){
        if (MarkerData.checkPointFlag[position]) {
            imageresource = item.imageResId
            text = item.textId
        }
    }
}