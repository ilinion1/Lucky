package com.example.portfolio

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class DataModel: ViewModel() {
    val message: MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
}