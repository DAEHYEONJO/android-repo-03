package com.example.gitreposearch.domain.repository

import com.example.gitreposearch.domain.model.response.GithubApiResponse
import com.example.gitreposearch.domain.model.Issue
import com.example.gitreposearch.domain.model.UserInfo
import com.example.gitreposearch.domain.model.notifications.Comment
import com.example.gitreposearch.domain.model.notifications.Notifications

interface GithubApiRepository {
    suspend fun getUserInfo(token: String): GithubApiResponse<UserInfo?>
    suspend fun getUserIssueList(token: String, state: String): GithubApiResponse<List<Issue>?>
    suspend fun getUserNotificationList(
        token: String,
        all: Boolean
    ): GithubApiResponse<List<Notifications>?>
    suspend fun getNotifiCommentCount(
        token: String,
        notification: Notifications
    ): GithubApiResponse<List<Comment>?>
    suspend fun changeNotificationAsRead(
        token: String,
        threadID: String
    ): GithubApiResponse<String?>
    fun getNotificationType(url: List<String>): String
    fun getNumber(url: List<String>): String
}