package com.example.gitreposearch.network

import com.example.gitreposearch.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GithubApiImpl {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.githubBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val githubApi: GithubApi = retrofit.create(GithubApi::class.java)

}