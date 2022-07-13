package com.example.gitreposearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.data.UserInfo
import com.example.gitreposearch.network.GithubApiResponse
import com.example.gitreposearch.repository.GithubApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: GithubApiRepository): ViewModel() {

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> get() = _userInfo

    fun getUserInfo(token: Token){
        viewModelScope.launch(Dispatchers.IO){
            repository.getUserInfo(token).apply {
                if (this is GithubApiResponse.Success){
                    _userInfo.postValue(data!!)
                }else if (this is GithubApiResponse.Error){
                    throw Exception("github getUserInfo exception code: $exceptionCode")
                }
            }
        }
    }
}