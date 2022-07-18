package com.example.gitreposearch.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitreposearch.data.Issue
import com.example.gitreposearch.data.notifications.Notifications
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.data.UserInfo
import com.example.gitreposearch.network.GithubApiResponse
import com.example.gitreposearch.repository.GithubApiRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: GithubApiRepository) : ViewModel() {

    companion object {
        const val TAG = "MainViewModel"
    }

    private var _currentTabState = MutableLiveData<String>("Issue")
    val currentTabState: LiveData<String> get() = _currentTabState

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> get() = _userInfo

    private val _userIssueList = MutableLiveData<List<Issue>>()
    val userIssueList: LiveData<List<Issue>> get() = _userIssueList

    private val _issueState = MutableLiveData<String>()
    val issueState: LiveData<String> get() = _issueState

    private val _userNotificationList = MutableLiveData<List<Notifications>>()
    val userNotificationList: LiveData<List<Notifications>> get() = _userNotificationList


    val token = MutableLiveData<Token>()


    fun changeState(state: String) {
        _currentTabState.value = state
    }


    fun getUserInfo(token: Token) {
        viewModelScope.launch {
            repository.getUserInfo(token).apply {
                if (this is GithubApiResponse.Success) {
                    _userInfo.value = data!!
<<<<<<< HEAD:app/src/main/java/com/example/gitreposearch/viewmodel/MainViewModel.kt
                } else if (this is GithubApiResponse.Error) {
=======
                }else if (this is GithubApiResponse.Error){
>>>>>>> feat/search_activity:app/src/main/java/com/example/gitreposearch/ui/viewmodel/MainViewModel.kt
                    throw Exception("github getUserInfo exception code: $exceptionCode")
                }
            }
        }
    }

<<<<<<< HEAD:app/src/main/java/com/example/gitreposearch/viewmodel/MainViewModel.kt
    fun getUserIssueList(token: Token) {
        viewModelScope.launch {
=======
    fun getUserIssueList(token : Token){
        viewModelScope.launch {

            withContext(Dispatchers.IO){

            }

        }
        viewModelScope.launch{
>>>>>>> feat/search_activity:app/src/main/java/com/example/gitreposearch/ui/viewmodel/MainViewModel.kt
            repository.getUserIssueList(token, issueState.value.toString().lowercase()).apply {
                if (this is GithubApiResponse.Success) {
                    _userIssueList.value = data!!
                } else if (this is GithubApiResponse.Error) {
                    throw Exception("github getUserIssueList exception code: $exceptionCode")
                }
            }
        }
    }

    fun setIssueState(state: String) {
        _issueState.value = state
    }

    fun getNotificationList(token : Token, all : Boolean) {
        Log.d(TAG, "getNotificationList: called")
        viewModelScope.launch {
            repository.getUserNotificationList(token,all).apply {
                if (this is GithubApiResponse.Success) {
                    _userNotificationList.value = data!!
                } else if (this is GithubApiResponse.Error) {
                    throw Exception("github getUserNotifcationList exception code: $exceptionCode")
                }
            }
        }
    }

}