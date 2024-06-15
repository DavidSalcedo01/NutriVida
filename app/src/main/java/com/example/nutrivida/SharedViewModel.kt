package com.example.nutrivida

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int> get() = _progress

    fun setProgress(value: Int) {
        _progress.value = value
    }
}
