package com.example.gitreposearch.network

import com.example.gitreposearch.data.Token
import com.example.gitreposearch.utils.Constants
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface GithubApi {

    @Headers("Accept: application/json")
    @POST(Constants.githubDomainUrl + "login/oauth/access_token")
    @FormUrlEncoded // @Field 형식을 사용하므로, Form이 encoding 되어야 한다.
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ): Response<Token>

}