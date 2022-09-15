package com.example.gitreposearch.data.remote.repository

import com.example.gitreposearch.data.remote.network.GithubApiImpl
import com.example.gitreposearch.domain.model.Token
import com.example.gitreposearch.domain.model.response.GithubApiResponse
import com.example.gitreposearch.domain.repository.GithubRepository

class GithubRepositoryImpl: GithubRepository {

    override suspend fun getUserToken(code: String): GithubApiResponse<Token?> {
        val response = GithubApiImpl.githubApi.getAccessToken(code = code)
        return if (response.isSuccessful){
            GithubApiResponse.Success(data = response.body())
        }else{
            GithubApiResponse.Error(exceptionCode = response.code())
        }
    }

}