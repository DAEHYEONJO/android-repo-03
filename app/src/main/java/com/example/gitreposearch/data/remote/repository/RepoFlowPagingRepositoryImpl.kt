package com.example.gitreposearch.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.gitreposearch.data.remote.repository.paging.RepoFlowPagingSource
import com.example.gitreposearch.domain.model.Repo
import com.example.gitreposearch.domain.repository.RepoFlowPagingRepository
import kotlinx.coroutines.flow.Flow

class RepoFlowPagingRepositoryImpl(
    private val pagingSource: RepoFlowPagingSource
): RepoFlowPagingRepository {

    override fun getRepoPaging(query: String): Flow<PagingData<Repo.Item>> {
        return Pager(
            defaultPagingConfig(),
            pagingSourceFactory = { pagingSource.apply { this.query = query} }
        ).flow
    }

    private fun defaultPagingConfig(): PagingConfig {
        return PagingConfig(
            pageSize = 30,
            enablePlaceholders = false
        )
    }
}