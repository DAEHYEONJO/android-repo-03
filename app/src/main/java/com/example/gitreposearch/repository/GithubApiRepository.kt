package com.example.gitreposearch.repository

import android.util.Log
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.data.UserInfo
import com.example.gitreposearch.network.GithubApiImpl
import com.example.gitreposearch.network.GithubApiResponse

class GithubApiRepository {

    companion object{
        const val TAG = "GithubApiRepository"
    }


    suspend fun getUserInfo(token: Token): GithubApiResponse<UserInfo?> {
        Log.e(TAG, "getUserInfo: ${token.tokenType} ${token.accessToken}", )
        val response = GithubApiImpl.githubApi.getUserInfo("${token.tokenType} ${token.accessToken}")
        Log.e(TAG, "getUserInfo: ${response}", )
        return if (response.isSuccessful){
            GithubApiResponse.Success(data = response.body())
        }else{
            GithubApiResponse.Error(exceptionCode = response.code())
        }
    }


}