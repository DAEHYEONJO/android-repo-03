package com.example.gitreposearch.ui.viewmodel

import android.util.Log
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

    companion object{
        const val TAG = "SearchViewModel"
    }

    private val _preQuery = MutableLiveData<String>()
    val preQuery: LiveData<String> get() = _preQuery

    val endOfListFlag = MutableLiveData<Boolean>(false)

    var repoList = MutableLiveData<PagingData<Repo.Item>>()

    fun getRepoPaging(query: String): Flow<PagingData<Repo.Item>> {
        Log.e(TAG, "getRepoPaging: $query", )
        _preQuery.value = query
        val ret = repoFlowRepository
            .getRepoPaging(query = query)
            .cachedIn(viewModelScope) 
        return ret
    }

//    fun getRepoPaging(query: String): Flow<PagingData<Repo.Item>> {
//        _preQuery.value = query
//        return repoFlowRepository
//            .getRepoPaging(query = query)
//            .cachedIn(viewModelScope).
//            stateIn(
//                viewModelScope,
//                SharingStarted.WhileSubscribed(5000),
//                Repo.Item()
//            )
//    }


}