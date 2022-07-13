package com.example.gitreposearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.repository.GithubApiRepository

class MainViewModel(private val repository: GithubApiRepository) : ViewModel() {

    private var _currentTabState = MutableLiveData("Issue")
    val currentTabState: LiveData<String>
        get() = _currentTabState

    val token = MutableLiveData<Token>()

    init {

    }

    fun changeState(state: String) {
        _currentTabState.value = state
    }
}