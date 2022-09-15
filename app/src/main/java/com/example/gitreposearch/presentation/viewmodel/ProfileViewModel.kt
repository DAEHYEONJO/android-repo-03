package com.example.gitreposearch.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.domain.repository.GithubApiRxRepository
import com.example.gitreposearch.presentation.UiState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class ProfileViewModel(
    private val githubApiRxRepository: GithubApiRxRepository
) : ViewModel() {

    private lateinit var disposable: Disposable
    private val token = GlobalApplication.getInstance().getTypedAccessToken()
    private val _userInfo = MutableLiveData<UiState>().apply {
        value = UiState.Loading
    }
    val userInfo: LiveData<UiState> get() = _userInfo

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        disposable = githubApiRxRepository.getUserInfoByRx(token!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { profileInfo ->
                    _userInfo.value = UiState.Success(profileInfo)
                },
                onError = { throwable ->
                    _userInfo.value = UiState.Error(throwable.message.toString())
                }
            )
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

}