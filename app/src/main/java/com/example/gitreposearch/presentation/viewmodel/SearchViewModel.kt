package com.example.gitreposearch.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gitreposearch.domain.model.Repo
import com.example.gitreposearch.domain.repository.RepoFlowPagingRepository
import kotlinx.coroutines.flow.Flow

class SearchViewModel(
    private val repoFlowRepository: RepoFlowPagingRepository
) : ViewModel() {

    companion object{
        const val TAG = "SearchViewModel"
    }

    val endOfListFlag = MutableLiveData<Boolean>(false)

    var repoList = MutableLiveData<PagingData<Repo.Item>>()

    fun getRepoPaging(query: String): Flow<PagingData<Repo.Item>> {
        return repoFlowRepository
            .getRepoPaging(query = query)
            .cachedIn(viewModelScope)
    }

}