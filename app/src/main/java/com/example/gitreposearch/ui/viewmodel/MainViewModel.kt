package com.example.gitreposearch.ui.viewmodel

import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.data.Issue
import com.example.gitreposearch.data.notifications.Notifications
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.data.UserInfo
import com.example.gitreposearch.data.notifications.Owner
import com.example.gitreposearch.data.notifications.Repository
import com.example.gitreposearch.data.notifications.Subject
import com.example.gitreposearch.network.GithubApiResponse
import com.example.gitreposearch.repository.GithubApiRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.FieldPosition
import kotlin.system.measureTimeMillis

class MainViewModel(private val repository: GithubApiRepository) : ViewModel() {

    private val token = GlobalApplication.getInstance().getTypedAccessToken()

    private var _currentTabState = MutableLiveData<String>("Issue")
    val currentTabState: LiveData<String> get() = _currentTabState

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> get() = _userInfo

    private val _userIssueList = MutableLiveData<List<Issue>>()
    val userIssueList: LiveData<List<Issue>> get() = _userIssueList

    private val _issueState = MutableLiveData<String>()
    val issueState: LiveData<String> get() = _issueState

    val userNotificationList = mutableListOf<Notifications>() // flow로 받아온 데이터 저장해놓을 리스트

    private val _userNotificationFlow = MutableStateFlow<Notifications>(
        value = Notifications(
            subject = Subject("", "", ""),
            repository = Repository(
                owner = Owner("", ""),
                "", ""
            ), updated_at = "", url = ""
        )
    )
    val userNotificationFlow : StateFlow<Notifications> = _userNotificationFlow

    private val initState = "init"


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
        userNotificationList.clear()
        viewModelScope.launch {
            repository.getUserNotificationList(token, false).apply {
                if (this is GithubApiResponse.Success) {
                    getNotifiCommentList(token, data!!)
                } else if (this is GithubApiResponse.Error) {
                    throw Exception("github getUserNotifcationList exception code: $exceptionCode")
                }
            }
        }
    }

    fun getNotifiCommentList(token: String, notificationList: List<Notifications>) {
        viewModelScope.launch {
            notificationList.forEach{ notification ->
                repository.getNotifiCommentCount(token, notification).apply {
                    if (this is GithubApiResponse.Success) {
                        notification.commentsCounts = data!!.size.toString()
                        _userNotificationFlow.emit(notification)
                        userNotificationList.add(notification)
                    } else if (this is GithubApiResponse.Error) {
                        throw Exception("github getUserNotifcationList exception code: $exceptionCode")
                    }
                }
            }
        }

    }

    fun changeNotificationAsRead( threadID: String) {
        viewModelScope.launch {
            repository.changeNotificationAsRead(token!!, threadID).apply {
                if (this is GithubApiResponse.Success) {
                    // TODO 응답별 처리
                }


            }
        }
    }

}