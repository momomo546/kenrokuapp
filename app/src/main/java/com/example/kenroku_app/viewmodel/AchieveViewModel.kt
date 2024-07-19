package com.example.kenroku_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kenroku_app.model.repositories.data.MarkerData

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
        val listSize = MarkerData.checkPointFlag.size
        val trueCount = MarkerData.checkPointFlag.count { it }
        _checkPointText.value = "$trueCount/$listSize"

        val variableValue = MarkerData.steps
        _walkCountText.value = "$variableValue"

        /*val mainActivity = activity as MainActivity
        val visitCount = mainActivity.visitCount
        _visitCountText.value = "${visitCount.getVisitCount()}"*/
    }
}