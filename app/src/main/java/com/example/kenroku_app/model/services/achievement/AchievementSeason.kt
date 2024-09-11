package com.example.kenroku_app.model.services.achievement

import com.example.kenroku_app.model.repositories.data.AchieveData
import com.example.kenroku_app.view.fragments.achieve.badge.BadgeData

class AchievementSeason(private val badgeData: List<BadgeData>, private val position:Int) : Achievement(badgeData) {
    private val item = badgeData[position]
    override fun checkAchievementConditions(){
        if (AchieveData.seasonFlag[position]) {
            imageresource = item.imageResId
            text = item.textId
        }
    }
}

class AchievementCheckPoint(private val badgeData: List<BadgeData>, private val position:Int) : Achievement(badgeData) {
    private val item = badgeData[position]
    override fun checkAchievementConditions(){
        if (AchieveData.checkPointFlag[position]) {
            imageresource = item.imageResId
            text = item.textId
        }
    }
}