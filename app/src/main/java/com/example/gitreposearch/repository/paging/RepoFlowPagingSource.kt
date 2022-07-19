package com.example.gitreposearch.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.data.Repo
import com.example.gitreposearch.network.GithubApi
import com.example.gitreposearch.utils.ConvertUtils

class RepoFlowPagingSource(
    private val githubApi: GithubApi
) : PagingSource<Int, Repo.Item>() {

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
        val nextKey = if (repoResponse.isSuccessful) currentPage + 1 else null
        return try {
            LoadResult.Page(
                data = responseBody!!,
                prevKey = preKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}
