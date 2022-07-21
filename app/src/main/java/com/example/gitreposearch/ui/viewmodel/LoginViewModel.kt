package com.example.gitreposearch.ui.viewmodel

import androidx.lifecycle.*
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.network.GithubApiResponse
import com.example.gitreposearch.repository.GithubRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val githubRepository: GithubRepository): ViewModel() {

    private val _token = MutableLiveData<Token>()
    val token: LiveData<Token> get() = _token

    private val _error = MutableLiveData(false)
    val error: LiveData<Boolean> get() = _error

    val startedLogin = MutableLiveData(false)

    fun getToken(code: String){
        viewModelScope.launch {
            githubRepository.getUserToken(code).apply {
                if (this is GithubApiResponse.Success){
                    _error.value = false
                    _token.value = data!!
                }else if (this is GithubApiResponse.Error){
                    _error.value = true
                }
            }
        }
    }
}