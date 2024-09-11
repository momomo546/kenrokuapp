package com.example.kenroku_app.model.services.achievement

import com.example.kenroku_app.R
import com.example.kenroku_app.view.fragments.achieve.badge.BadgeData

open class Achievement(badgeData: List<BadgeData>) {
    var imageresource = R.drawable.badge_default
    var text = R.string.text

    open fun checkAchievementConditions(){}


}