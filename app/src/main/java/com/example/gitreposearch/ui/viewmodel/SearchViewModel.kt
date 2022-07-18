package com.example.gitreposearch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gitreposearch.data.Repo
import com.example.gitreposearch.repository.RepoFlowPagingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(
    private val repoFlowRepository: RepoFlowPagingRepository
) : ViewModel() {

    fun getRepoPaging(query: String): Flow<PagingData<Repo.Item>> =
        repoFlowRepository.getRepoPaging(query = query).cachedIn(viewModelScope)
}