package com.example.gitreposearch.domain.repository

import com.example.gitreposearch.domain.model.ProfileInfo
import io.reactivex.rxjava3.core.Single

interface GithubApiRxRepository {
    fun getUserInfoByRx(token: String): Single<ProfileInfo>
}