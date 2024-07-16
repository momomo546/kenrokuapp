package com.example.kenroku_app.services.achievement

import com.example.kenroku_app.R
import com.example.kenroku_app.fragments.achieve.badge.MyData

open class Achievement(myData: List<MyData>) {
    var imageresource = R.drawable.badge_default
    var text = "test"

    open fun checkAchievementConditions(){}


}