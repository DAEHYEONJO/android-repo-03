package com.example.gitreposearch

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.gitreposearch.network.GithubApiImpl
import com.example.gitreposearch.repository.GithubApiRepository
import com.example.gitreposearch.repository.GithubRepository
import com.example.gitreposearch.repository.RepoFlowPagingRepository
import com.example.gitreposearch.repository.paging.RepoFlowPagingSource

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

        private lateinit var pagingSource: RepoFlowPagingSource
        lateinit var repoFlowRepository: RepoFlowPagingRepository
    }

    override fun onCreate() {
        super.onCreate()
        initProperties()
    }

    private fun initProperties() {
        pagingSource = RepoFlowPagingSource(GithubApiImpl.githubApi)
        repoFlowRepository = RepoFlowPagingRepository(pagingSource)
        githubRepository = GithubRepository()
        githubApiRepository = GithubApiRepository()
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