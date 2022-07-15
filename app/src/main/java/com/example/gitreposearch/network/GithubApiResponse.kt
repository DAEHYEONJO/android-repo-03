package com.example.gitreposearch.network

import kotlin.Exception

sealed class GithubApiResponse<T>(
    data: T? = null,
    exception: Int = -1
) {
    data class Success<T>(val data: T) : GithubApiResponse<T>(data = data)
    data class Error<T>(val exceptionCode: Int) : GithubApiResponse<T>(exception = exceptionCode)
}
