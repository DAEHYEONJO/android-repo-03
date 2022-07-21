package com.example.gitreposearch.repository

import android.app.Notification
import android.text.TextUtils.substring
import android.util.Log
import com.example.gitreposearch.data.Issue
import com.example.gitreposearch.data.notifications.Notifications
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.data.UserInfo
import com.example.gitreposearch.data.notifications.Comment
import com.example.gitreposearch.data.notifications.CommentsList
import com.example.gitreposearch.data.notifications.Type
import com.example.gitreposearch.network.GithubApiImpl
import com.example.gitreposearch.network.GithubApiResponse
import com.example.gitreposearch.utils.Constants
import retrofit2.Response

class GithubApiRepository {

    suspend fun getUserInfo(token: String): GithubApiResponse<UserInfo?> {
        val responseUser = GithubApiImpl.githubApi.getUserInfo(token)
        return if (responseUser.isSuccessful) {
            val responseUserBody = responseUser.body()
            val loginName = responseUserBody?.login
            val responseStarred = GithubApiImpl.githubApi.getStarred(
                token,
                loginName!!
            )
            GithubApiResponse.Success(data = responseUserBody.apply {
                starredCount = responseStarred.body()?.size ?: 0
            })
        } else {
            GithubApiResponse.Error(exceptionCode = responseUser.code())
        }
    }

    suspend fun getUserIssueList(token: String, state: String): GithubApiResponse<List<Issue>?> {
        val response = GithubApiImpl.githubApi.getUserIssueList(
            token,
            state
        )
        return if (response.isSuccessful) {
            GithubApiResponse.Success(data = response.body())
        } else {
            GithubApiResponse.Error(exceptionCode = response.code())
        }
    }

    suspend fun getUserNotificationList(
        token: String,
        all: Boolean
    ): GithubApiResponse<List<Notifications>?> {
        val response = GithubApiImpl.githubApi.getUserNotificationList(
            token, all
        )

        return if (response.isSuccessful) {
            val responseNotificationsBody = response.body() // notification List body   
            responseNotificationsBody?.forEach { notification -> // notification 개수만큼 반복
                val url = notification.subject.url.split("/")
                val type = getNotificationType(url)
                notification.number = getNumber(url)
                notification.threadID = notification.url.split("/").last()

                val responseCommentsList = GithubApiImpl.githubApi.getCommentsList(
                    token,
                    notification.repository.owner.login,
                    notification.repository.name,
                    type,
                    notification.number
                )

                if (responseCommentsList != null && responseCommentsList.isSuccessful) {
                    notification.commentsCounts = responseCommentsList.body()!!.size.toString()
                }
            }
            GithubApiResponse.Success(data = response.body())
        } else {
            GithubApiResponse.Error(exceptionCode = response.code())
        }
    }

    suspend fun changeNotificationAsRead(token : String, threadID : String): GithubApiResponse<String?> {
        val response = GithubApiImpl.githubApi.changeNotificationAsRead(token, threadID)
        return if(response.isSuccessful) {
            Log.d("Text", "changeNotificationAsRead: ${response.code()}")
            GithubApiResponse.Success(data = response.body())

        }
        else {
            GithubApiResponse.Error(exceptionCode = response.code())
        }
    }

    private fun getNotificationType(url : List<String>) : String {
        return url[url.size - 2]
    }

    private fun getNumber(url : List<String>): String {
        return url.last()
    }

}