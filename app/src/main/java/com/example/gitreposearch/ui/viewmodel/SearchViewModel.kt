package com.example.gitreposearch.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gitreposearch.data.Repo
import com.example.gitreposearch.repository.RepoFlowPagingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(
    private val repoFlowRepository: RepoFlowPagingRepository
) : ViewModel() {

    private val _preQuery = MutableLiveData<String>()
    val preQuery: LiveData<String> get() = _preQuery

    lateinit var repoList: LiveData<PagingData<Repo.Item>>

    fun getRepoPaging(query: String) {
        _preQuery.value = query
        repoList = repoFlowRepository
            .getRepoPaging(query = query)
            .cachedIn(viewModelScope).asLiveData()
    }


}