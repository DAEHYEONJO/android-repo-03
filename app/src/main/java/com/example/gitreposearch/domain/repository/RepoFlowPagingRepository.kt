package com.example.gitreposearch.domain.repository

import androidx.paging.PagingData
import com.example.gitreposearch.domain.model.Repo
import kotlinx.coroutines.flow.Flow

interface RepoFlowPagingRepository {
    fun getRepoPaging(query: String): Flow<PagingData<Repo.Item>>
}