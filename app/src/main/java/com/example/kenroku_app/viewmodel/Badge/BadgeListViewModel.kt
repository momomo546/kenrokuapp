package com.example.kenroku_app.viewmodel.Badge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kenroku_app.R
import com.example.kenroku_app.view.fragments.achieve.badge.BadgeData

class BadgeListViewModel : ViewModel() {

    private val _badgeList = MutableLiveData<List<BadgeData>>().apply {
        value = listOf(
            BadgeData(R.drawable.img_marker_04, R.string.spring_badge),
            BadgeData(R.drawable.img_marker_06, R.string.summer_badge),
            BadgeData(R.drawable.img_marker_10, R.string.fall_badge),
            BadgeData(R.drawable.img_marker_12, R.string.winter_badge),
        )
    }
    val badgeList: LiveData<List<BadgeData>> = _badgeList
}