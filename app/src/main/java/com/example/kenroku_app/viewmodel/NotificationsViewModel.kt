package com.example.kenroku_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "今後イベント情報や周辺観光地情報を\n掲載予定です。"
    }
    val text: LiveData<String> = _text
}