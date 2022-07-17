package com.example.gitreposearch.repository

import android.util.Log
import com.example.gitreposearch.data.Issue
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.data.UserInfo
import com.example.gitreposearch.network.GithubApiImpl
import com.example.gitreposearch.network.GithubApiResponse

class GithubApiRepository {

    companion object {
        const val TAG = "GithubApiRepository"
    }

    suspend fun getUserInfo(token: Token): GithubApiResponse<UserInfo?> {
        val responseUser = GithubApiImpl.githubApi.getUserInfo("${token.tokenType} ${token.accessToken}")
        return if (responseUser.isSuccessful) {
            val responseUserBody = responseUser.body()
            val loginName = responseUserBody?.login
            val responseStarred = GithubApiImpl.githubApi.getStarred(
                "${token.tokenType} ${token.accessToken}",
                loginName!!
            )
            GithubApiResponse.Success(data = responseUserBody.apply {
                starredCount = responseStarred.body()?.size ?: 0
            })
        } else {
            GithubApiResponse.Error(exceptionCode = responseUser.code())
        }
    }

    suspend fun getUserIssueList(token: Token, state : String): GithubApiResponse<List<Issue>?> {
        Log.d("jiwoo", "getUserIssueList: API REPO")
        val response = GithubApiImpl.githubApi.getUserIssueList("${token.tokenType} ${token.accessToken}", state)
        return if (response.isSuccessful){
            GithubApiResponse.Success(data = response.body())
        }else{
            GithubApiResponse.Error(exceptionCode = response.code())
        }
    }



}