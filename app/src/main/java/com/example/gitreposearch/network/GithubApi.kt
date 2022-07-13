package com.example.gitreposearch.network

import com.example.gitreposearch.BuildConfig
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.data.UserInfo
import com.example.gitreposearch.utils.Constants
import retrofit2.Response
import retrofit2.http.*

interface GithubApi {

    @Headers("Accept: application/json")
    @POST(Constants.githubDomainUrl + "login/oauth/access_token") // base url과 상이해서 full url 작성함
    @FormUrlEncoded // @Field 형식을 사용하므로, Form이 encoding 되어야 한다.
    suspend fun getAccessToken(
        @Field("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Field("client_secret") clientSecret: String = BuildConfig.CLIENT_SECRET,
        @Field("code") code: String
    ): Response<Token>

    @Headers("Accept: application/json")
    @GET("user")
    suspend fun getUserInfo(
        @Header("Authorization") tokenWithTokenType: String
    ): Response<UserInfo>




}