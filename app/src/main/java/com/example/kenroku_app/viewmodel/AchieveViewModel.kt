package com.example.kenroku_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AchieveViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is achieve Fragment"
    }
    val text: LiveData<String> = _text
}