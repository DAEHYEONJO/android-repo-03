package com.example.gitreposearch.viewmodel

import androidx.lifecycle.*
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.network.GithubApiResponse
import com.example.gitreposearch.repository.GithubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val githubRepository: GithubRepository): ViewModel() {

    private val _token = MutableLiveData<Token>()
    val token: LiveData<Token> get() = _token

    fun getToken(code: String){
        viewModelScope.launch {
            githubRepository.getUserToken(code).apply {
                if (this is GithubApiResponse.Success){
                    _token.postValue(data!!)
                }else if (this is GithubApiResponse.Error){
                    throw Exception("github getToken exception code: $exceptionCode")
                }
            }
        }
    }
}