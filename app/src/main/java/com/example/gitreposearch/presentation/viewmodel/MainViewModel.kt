package com.example.gitreposearch.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.domain.model.response.GithubApiResponse
import com.example.gitreposearch.domain.model.Issue
import com.example.gitreposearch.domain.model.UserInfo
import com.example.gitreposearch.domain.model.notifications.Notifications
import com.example.gitreposearch.domain.repository.GithubApiRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: GithubApiRepository) : ViewModel() {

    private val token = GlobalApplication.getInstance().getTypedAccessToken()

    private var _currentTabState = MutableLiveData("Issue")
    val currentTabState: LiveData<String> get() = _currentTabState

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> get() = _userInfo

    private val _userIssueList = MutableLiveData<List<Issue>>()
    val userIssueList: LiveData<List<Issue>> get() = _userIssueList

    private val _issueState = MutableLiveData<String>()
    val issueState: LiveData<String> get() = _issueState

    private val _userNotificationList =
        MutableLiveData<MutableList<Notifications>>() // flow로 받아온 데이터 저장해놓을 리스트
    val userNotificationList: LiveData<MutableList<Notifications>> get() = _userNotificationList

    private val _commentInfo = MutableLiveData<Pair<Int,String>>()
    val commentInfo : LiveData<Pair<Int,String>> get() = _commentInfo

    init {
        getUserInfo(token!!)
        getNotificationList(token)
    }

    fun changeState(state: String) {
        _currentTabState.value = state
    }

    fun getUserInfo(token: String) {
        viewModelScope.launch {
            repository.getUserInfo(token).apply {
                if (this is GithubApiResponse.Success) {
                    _userInfo.value = data!!
                } else if (this is GithubApiResponse.Error) {
                    throw Exception("github getUserInfo exception code: $exceptionCode")
                }
            }
        }
    }

    fun getUserIssueList(token: String) {
        viewModelScope.launch {
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

    fun getNotificationList(token: String) {
        viewModelScope.launch {
            repository.getUserNotificationList(token, false).apply {
                if (this is GithubApiResponse.Success) {
                    getNotifiCommentList(token, data!!)
                    _userNotificationList.value = data!!.toMutableList()
                } else if (this is GithubApiResponse.Error) {
                    throw Exception("github getUserNotifcationList exception code: $exceptionCode")
                }
            }
        }
    }

    fun getNotifiCommentList(token: String, notificationList: List<Notifications>) {
        notificationList.forEachIndexed {idx, notification ->
            viewModelScope.launch {
                repository.getNotifiCommentCount(token, notification).apply {
                    if (this is GithubApiResponse.Success) {
                        notification.commentsCounts = data!!.size.toString()
                        _commentInfo.value = Pair(idx, data!!.size.toString())
                    } else if (this is GithubApiResponse.Error) {
                        if(exceptionCode == 404){
                            notification.commentsCounts = "0"
                        }
                        //throw Exception("github getUserNotifcationList exception code: $exceptionCode")
                    }
                }
            }
        }
    }

    fun changeNotificationAsRead(position : Int) {
        val threadID = userNotificationList.value!![position].threadID
        viewModelScope.launch {
            repository.changeNotificationAsRead(token!!, threadID).apply {
                if (this is GithubApiResponse.Success) {

                }
            }
        }
    }

}