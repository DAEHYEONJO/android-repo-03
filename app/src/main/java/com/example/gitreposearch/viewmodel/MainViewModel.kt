package com.example.gitreposearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(initState: String) : ViewModel() {
    private var _currentTabState = MutableLiveData<String>()

    val currentTabState: LiveData<String>
        get() = _currentTabState


    init {
        _currentTabState.value = initState
    }
    fun changeState(state : String){
        _currentTabState.value = state
    }
}