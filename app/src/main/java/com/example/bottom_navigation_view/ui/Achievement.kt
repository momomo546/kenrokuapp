package com.example.bottom_navigation_view.ui

import com.example.bottom_navigation_view.R
import com.example.bottom_navigation_view.ui.dashboard.MyData

open class Achievement(myData: List<MyData>) {
    var imageresource = R.drawable.badge_default
    var text = "test"

    open fun checkAchievementConditions(){}


}