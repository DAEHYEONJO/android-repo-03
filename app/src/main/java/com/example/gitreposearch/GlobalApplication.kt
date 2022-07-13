package com.example.gitreposearch

import android.app.Application
import android.content.Context
import com.example.gitreposearch.repository.GithubApiRepository
import com.example.gitreposearch.repository.GithubRepository

class GlobalApplication: Application(){

    init {
        instance = this
    }

    companion object{
        private lateinit var instance: GlobalApplication
        fun getInstance(): Context {
            return instance.applicationContext
        }

        lateinit var githubRepository: GithubRepository
        lateinit var githubApiRepository: GithubApiRepository
    }

    override fun onCreate() {
        super.onCreate()
        initProperties()
    }

    private fun initProperties() {
        githubRepository = GithubRepository()
        githubApiRepository = GithubApiRepository()
    }

}