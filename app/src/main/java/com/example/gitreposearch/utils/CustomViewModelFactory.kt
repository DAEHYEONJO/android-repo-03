package com.example.gitreposearch.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.repository.GithubApiRepository
import com.example.gitreposearch.repository.GithubRepository
import com.example.gitreposearch.repository.RepoFlowPagingRepository
import com.example.gitreposearch.ui.viewmodel.LoginViewModel
import com.example.gitreposearch.ui.viewmodel.MainViewModel
import com.example.gitreposearch.ui.viewmodel.SearchViewModel

class CustomViewModelFactory<X> (private val repository: X): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when(repository){
            is GithubRepository ->  LoginViewModel(GlobalApplication.githubRepository) as T
            is GithubApiRepository -> MainViewModel(GlobalApplication.githubApiRepository) as T
            is RepoFlowPagingRepository -> SearchViewModel(GlobalApplication.repoFlowRepository) as T
            else -> super.create(modelClass)
        }
    }
}