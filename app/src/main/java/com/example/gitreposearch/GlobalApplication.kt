package com.example.gitreposearch

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.gitreposearch.data.remote.network.GithubApiImpl
import com.example.gitreposearch.data.remote.repository.GithubApiRepositoryImpl
import com.example.gitreposearch.data.remote.repository.GithubApiRxRepositoryImpl
import com.example.gitreposearch.data.remote.repository.GithubRepositoryImpl
import com.example.gitreposearch.data.remote.repository.RepoFlowPagingRepositoryImpl
import com.example.gitreposearch.data.remote.repository.paging.RepoFlowPagingSource
import com.example.gitreposearch.domain.repository.GithubApiRepository
import com.example.gitreposearch.domain.repository.GithubApiRxRepository
import com.example.gitreposearch.domain.repository.GithubRepository
import com.example.gitreposearch.domain.repository.RepoFlowPagingRepository

class GlobalApplication: Application(){

    init {
        instance = this
    }

    companion object{
        private lateinit var instance: GlobalApplication
        fun getInstance(): GlobalApplication {
            return instance
        }

        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor
        lateinit var githubRepository: GithubRepository
        lateinit var githubApiRepository: GithubApiRepository
        lateinit var githubApiRxRepository: GithubApiRxRepository
        private lateinit var pagingSource: RepoFlowPagingSource
        lateinit var repoFlowRepository: RepoFlowPagingRepository
    }

    override fun onCreate() {
        super.onCreate()
        initProperties()
    }

    private fun initProperties() {
        githubApiRxRepository = GithubApiRxRepositoryImpl()
        pagingSource = RepoFlowPagingSource(GithubApiImpl.githubApi)
        repoFlowRepository = RepoFlowPagingRepositoryImpl(pagingSource)
        githubRepository = GithubRepositoryImpl()
        githubApiRepository = GithubApiRepositoryImpl()
        sharedPreferences = getSharedPreferences(resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun putTypedAccessToken(typedAccessToken: String) {
        with(editor){
            putString("typedAccessToken", typedAccessToken)
            apply()
        }
    }

    fun getTypedAccessToken(): String? = sharedPreferences.getString("typedAccessToken", "")

}