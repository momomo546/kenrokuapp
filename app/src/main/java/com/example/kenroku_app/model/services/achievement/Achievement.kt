package com.example.kenroku_app.model.services.achievement

import com.example.kenroku_app.R
import com.example.kenroku_app.view.fragments.achieve.badge.MyData

open class Achievement(myData: List<MyData>) {
    var imageresource = R.drawable.badge_default
    var text = "test"

    open fun checkAchievementConditions(){}


}