package com.example.gitreposearch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.repository.GithubApiRepository
import com.example.gitreposearch.repository.GithubRepository

class CustomViewModelFactory<X> (private val repository: X): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when(repository){
            is GithubRepository ->  LoginViewModel(GlobalApplication.githubRepository) as T
            is GithubApiRepository -> ProfileViewModel(GlobalApplication.githubApiRepository) as T
            else -> super.create(modelClass)
        }
    }
}