package com.example.kenroku_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kenroku_app.model.repositories.data.AchieveData

class AchieveViewModel : ViewModel() {

    private val _checkPointText = MutableLiveData<String>().apply {
        value = "This is achieve Fragment"
    }
    private val _walkCountText = MutableLiveData<String>().apply {
        value = "This is achieve Fragment"
    }
    private val _visitCountText = MutableLiveData<String>().apply {
        value = "This is achieve Fragment"
    }

    val checkPointText: LiveData<String> = _checkPointText
    val walkCountText: LiveData<String> = _walkCountText
    val visitCountText: LiveData<String> = _visitCountText

    fun viewUpdate(){
        val listSize = AchieveData.checkPointFlag.size
        val trueCount = AchieveData.checkPointFlag.count { it }
        _checkPointText.value = "$trueCount/$listSize"

        val variableValue = AchieveData.steps
        _walkCountText.value = "$variableValue"

        val visitCount = AchieveData.visitCount
        _visitCountText.value = "$visitCount"
    }
}