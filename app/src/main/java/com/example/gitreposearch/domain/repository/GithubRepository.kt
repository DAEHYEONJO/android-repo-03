package com.example.gitreposearch.domain.repository

import com.example.gitreposearch.domain.model.Token
import com.example.gitreposearch.domain.model.response.GithubApiResponse

interface GithubRepository {
    suspend fun getUserToken(code: String): GithubApiResponse<Token?>
}