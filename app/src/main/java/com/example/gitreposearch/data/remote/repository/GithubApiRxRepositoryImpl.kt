package com.example.gitreposearch.data.remote.repository

import com.example.gitreposearch.data.remote.network.GithubApiImpl
import com.example.gitreposearch.domain.model.ProfileInfo
import com.example.gitreposearch.domain.repository.GithubApiRxRepository
import io.reactivex.rxjava3.core.Single

class GithubApiRxRepositoryImpl : GithubApiRxRepository {
    override fun getUserInfoByRx(token: String): Single<ProfileInfo> {
        return GithubApiImpl.githubApi.getUserInfoByRx(token).flatMap { userInfo ->
            GithubApiImpl.githubApi.getStarredByRx(
                token,
                userInfo.login!!
            ).map { starred ->
                ProfileInfo(
                    avatarUrl = userInfo.avatarUrl!!,
                    login = userInfo.login,
                    name = userInfo.name!!,
                    bio = userInfo.bio!!,
                    location = userInfo.location!!,
                    blog = userInfo.blog!!,
                    email = userInfo.email!!,
                    followers = userInfo.followers.toString(),
                    following = userInfo.following.toString(),
                    repositoryCount = (userInfo.publicRepos + userInfo.totalPrivateRepos).toString(),
                    starredCount = starred.size.toString()
                )
            }
        }
    }
}