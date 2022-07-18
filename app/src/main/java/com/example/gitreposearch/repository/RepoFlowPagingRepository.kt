package com.example.gitreposearch.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.gitreposearch.data.Repo
import com.example.gitreposearch.repository.paging.RepoFlowPagingSource
import kotlinx.coroutines.flow.Flow

class RepoFlowPagingRepository(
    private val pagingSource: RepoFlowPagingSource
) {

    fun getRepoPaging(query: String): Flow<PagingData<Repo.Item>> {
        return Pager(
            defaultPagingConfig(),
            pagingSourceFactory = { pagingSource.apply { this.query = query} }
        ).flow
    }

    private fun defaultPagingConfig(): PagingConfig {
        return PagingConfig(
            pageSize = 30,
            prefetchDistance = 30,
            enablePlaceholders = false,
            initialLoadSize = 90,
            maxSize = 150
        )
    }
}