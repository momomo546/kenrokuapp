package com.example.kenroku_app.viewmodel.Badge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kenroku_app.R

class BadgeViewModel : ViewModel() {

    private val IMAGE_RESOURCE =  R.drawable.badge_default
    private val TEXT = R.string.text

    private val _imageBadge = MutableLiveData<Int>().apply {
        value = IMAGE_RESOURCE
    }
    private val _textBadge = MutableLiveData<Int>().apply {
        value = TEXT
    }
    val imageBadge: LiveData<Int> = _imageBadge
    val textBadge: LiveData<Int> = _textBadge

}