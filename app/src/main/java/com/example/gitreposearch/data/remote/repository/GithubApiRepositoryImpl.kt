package com.example.gitreposearch.data.remote.repository

import com.example.gitreposearch.domain.model.Issue
import com.example.gitreposearch.domain.model.notifications.Notifications
import com.example.gitreposearch.domain.model.UserInfo
import com.example.gitreposearch.domain.model.notifications.Comment
import com.example.gitreposearch.data.remote.network.GithubApiImpl
import com.example.gitreposearch.domain.model.response.GithubApiResponse
import com.example.gitreposearch.domain.repository.GithubApiRepository

class GithubApiRepositoryImpl: GithubApiRepository {

    override suspend fun getUserInfo(token: String): GithubApiResponse<UserInfo?> {
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

    override suspend fun getUserIssueList(token: String, state: String): GithubApiResponse<List<Issue>?> {
        val response = GithubApiImpl.githubApi.getUserIssueList(token, state)

        return if (response.isSuccessful) {
            GithubApiResponse.Success(data = response.body())
        } else {
            GithubApiResponse.Error(exceptionCode = response.code())
        }
    }

    override suspend fun getUserNotificationList(
        token: String,
        all: Boolean
    ): GithubApiResponse<List<Notifications>?> {
        val response = GithubApiImpl.githubApi.getUserNotificationList(token, all)

        return if (response.isSuccessful) {
            GithubApiResponse.Success(data = response.body())
        } else {
            GithubApiResponse.Error(exceptionCode = response.code())
        }
    }

    override suspend fun getNotifiCommentCount(
        token: String,
        notification: Notifications
    ): GithubApiResponse<List<Comment>?> {
        val url = notification.subject.url.split("/")
        val type = getNotificationType(url)
        notification.number = getNumber(url)
        notification.threadID = notification.url.split("/").last()

        val response = GithubApiImpl.githubApi.getCommentsList(
            token,
            notification.repository.owner.login,
            notification.repository.name,
            type,
            notification.number
        )
        return if (response.isSuccessful) {
            GithubApiResponse.Success(data = response.body())
        } else {
            GithubApiResponse.Error(exceptionCode = response.code())
        }

    }

    override suspend fun changeNotificationAsRead(
        token: String,
        threadID: String
    ): GithubApiResponse<String?> {
        val response = GithubApiImpl.githubApi.changeNotificationAsRead(token, threadID)
        return if (response.isSuccessful) {
            GithubApiResponse.Success(data = response.body())
        } else {
            GithubApiResponse.Error(exceptionCode = response.code())
        }
    }


    override fun getNotificationType(url: List<String>): String {
        return url[url.size - 2]
    }

    override fun getNumber(url: List<String>): String {
        return url.last()
    }

}