package com.example.gitreposearch.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.data.Repo
import com.example.gitreposearch.network.GithubApi

class RepoFlowPagingSource(
    private val githubApi: GithubApi
): PagingSource<Int, Repo.Item>() {

    var query: String = ""

    override fun getRefreshKey(state: PagingState<Int, Repo.Item>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo.Item> {
        val currentPage = params.key ?: 1
        val repoResponse = githubApi.getRepoByQuery(
            typedAccessToken = GlobalApplication.getInstance().getTypedAccessToken()!!,
            query = query,
            page = currentPage
        )
        val preKey = if (currentPage == 1) null else currentPage - 1
        val nextKey = if (repoResponse.isSuccessful) currentPage + 1 else null
        return try{
            LoadResult.Page(
                data = repoResponse.body()!!.items,
                prevKey = preKey,
                nextKey = nextKey
            )
        }catch (e : Exception){
            LoadResult.Error(e)
        }
    }

}
