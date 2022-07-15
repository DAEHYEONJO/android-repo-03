package com.example.gitreposearch.repository

import com.example.gitreposearch.BuildConfig
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.data.UserInfo
import com.example.gitreposearch.network.GithubApiImpl
import com.example.gitreposearch.network.GithubApiResponse

class GithubRepository {

    suspend fun getUserToken(code: String): GithubApiResponse<Token?> {
        val response = GithubApiImpl.githubApi.getAccessToken(code = code)
        return if (response.isSuccessful){
            GithubApiResponse.Success(data = response.body())
        }else{
            GithubApiResponse.Error(exceptionCode = response.code())
        }
    }

}