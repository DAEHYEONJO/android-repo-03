package com.example.gitreposearch.data.remote.repository.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.domain.model.Repo
import com.example.gitreposearch.data.remote.network.GithubApi
import com.example.gitreposearch.utils.ConvertUtils

class RepoFlowPagingSource(
    private val githubApi: GithubApi
) : PagingSource<Int, Repo.Item>() {

    companion object {
        const val TAG = "RepoFlowPagingSource"
    }

    var query: String = ""
    private val colorStringSet = mutableSetOf<String>()
    private val colorStringHashMap = mutableMapOf<String, String>()

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

        if (!repoResponse.isSuccessful) {
            val errorMsg = ConvertUtils.getErrorResponseMsg(repoResponse.errorBody()!!)
            return LoadResult.Error(Throwable(errorMsg))
        }else if (repoResponse.body()?.items!!.isEmpty() ){
            return if (currentPage==1) LoadResult.Error(Throwable("Empty Result"))
            else LoadResult.Error(Throwable("End of List"))
        }

        val responseBody = repoResponse.body()?.items?.asSequence()
            ?.onEach {
                it.stargazersCountString = ConvertUtils.getStarredString(it.stargazersCount)
                it.colorString = if (colorStringHashMap.containsKey(it.language)) {
                    val colorString = colorStringHashMap[it.language]
                    colorString!!
                } else {
                    var randomColorString = ""

                    while (colorStringSet.contains(
                            ConvertUtils.getRandomColorCode()
                                .also { randStr -> randomColorString = randStr })
                    );

                    colorStringSet.add(randomColorString)
                    colorStringHashMap[it.language] = randomColorString
                    randomColorString
                }
            }?.toList()

        val preKey = if (currentPage == 1) null else currentPage - 1
        val nextKey = if (repoResponse.code()==200) currentPage + 1 else null
        Log.e(TAG, "load: $preKey $currentPage $nextKey", )

        return LoadResult.Page(
            data = responseBody!!,
            prevKey = preKey,
            nextKey = nextKey
        )
    }

}
