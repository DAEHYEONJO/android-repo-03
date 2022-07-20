package com.example.gitreposearch.ui.viewmodel

import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.FieldPosition
import kotlin.system.measureTimeMillis

class MainViewModel(private val repository: GithubApiRepository) : ViewModel() {

    private var _currentTabState = MutableLiveData<String>("Issue")
    val currentTabState: LiveData<String> get() = _currentTabState

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> get() = _userInfo

    private val _userIssueList = MutableLiveData<List<Issue>>()
    val userIssueList: LiveData<List<Issue>> get() = _userIssueList

    private val _issueState = MutableLiveData<String>()
    val issueState: LiveData<String> get() = _issueState

    private val _userNotificationList = MutableLiveData<MutableList<Notifications>>()
    val userNotificationList: LiveData<MutableList<Notifications>> get() = _userNotificationList

    val userNotificationFlow = MutableStateFlow<Notifications>(
        value = Notifications(
            subject = Subject("","",""),
            repository = Repository(
                owner = Owner("",""),
                "",""
            ), updated_at = "", url = ""
        )
    )

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
                    //_userNotificationList.value = data!! as MutableList<Notifications>
                } else if (this is GithubApiResponse.Error) {
                    throw Exception("github getUserNotifcationList exception code: $exceptionCode")
                }
            }
        }
    }

    private suspend fun getNotifiCommentList(token: String, notificationList: List<Notifications>) {
        notificationList.forEachIndexed { i, notification ->
            viewModelScope.launch {
                repository.getNotifiCommentCount(token, notification).apply {
                    if (this is GithubApiResponse.Success) {
                        Log.d("jiwoo", "getNotifiCommentList: $i")
                        notification.commentsCounts = data!!.size.toString()
                        userNotificationFlow.emit(notification)
                    } else if (this is GithubApiResponse.Error) {
                        throw Exception("github getUserNotifcationList exception code: $exceptionCode")
                    }
                }
            }
        }

    }


    fun changeNotificationAsRead(position: Int, token: String) {
        val threadID = userNotificationList.value!![position].threadID
        viewModelScope.launch {
            repository.changeNotificationAsRead(token, threadID).apply {
                if (this is GithubApiResponse.Success) {
                    // TODO 응답별 처리
                }


            }
        }
    }

}