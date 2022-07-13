package com.example.gitreposearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitreposearch.data.Issue
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.data.UserInfo
import com.example.gitreposearch.network.GithubApiResponse
import com.example.gitreposearch.repository.GithubApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: GithubApiRepository) : ViewModel() {

    private var _currentTabState = MutableLiveData("Issue")
    val currentTabState: LiveData<String> get() = _currentTabState

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> get() = _userInfo

    private val _userIssueList = MutableLiveData<List<Issue>>()
    val userIssueList : LiveData<List<Issue>> get() = _userIssueList

    val token = MutableLiveData<Token>()


    fun changeState(state: String) {
        _currentTabState.value = state
    }

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

    fun getUserIssueList(token : Token){
        viewModelScope.launch(Dispatchers.IO){
            repository.getUserIssueList(token).apply {
                if(this is GithubApiResponse.Success){
                    _userIssueList.postValue(this.data!!)
                }
                else{
                    Log.d("Reponse ", "실패")
                }
            }
        }
    }

}