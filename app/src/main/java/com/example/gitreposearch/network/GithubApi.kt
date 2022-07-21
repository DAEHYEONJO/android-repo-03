package com.example.gitreposearch.network

import com.example.gitreposearch.BuildConfig
import com.example.gitreposearch.data.Issue
import com.example.gitreposearch.data.notifications.Notifications
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.data.UserInfo
import com.example.gitreposearch.data.notifications.Comment
import com.example.gitreposearch.data.*
import com.example.gitreposearch.utils.Constants
import retrofit2.Response
import retrofit2.http.*

interface GithubApi {

    @POST(Constants.githubDomainUrl + "login/oauth/access_token")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Field("client_secret") clientSecret: String = BuildConfig.CLIENT_SECRET,
        @Field("code") code: String
    ): Response<Token>

    @GET("user")
    suspend fun getUserInfo(
        @Header("Authorization") typedAccessToken: String
    ): Response<UserInfo>

    @GET("/users/{user}/starred")
    suspend fun getStarred(
        @Header("Authorization") typedAccessToken: String,
        @Path("user") user: String
    ): Response<Starred>

    @GET("issues")
    suspend fun getUserIssueList(
        @Header("Authorization") typedAccessToken : String,
        @Query("state") state:String,
        @Query("filter") filter:String = "all"
    ): Response<List<Issue>>

    @GET("notifications")
    suspend fun getUserNotificationList(
        @Header("Authorization") typedAccessToken : String,
        @Query("all") all:Boolean
    ): Response<List<Notifications>>

    @GET("/repos/{owner}/{repo}/{type}/{number}/comments")
    suspend fun getCommentsList(
        @Header("Authorization") typedAccessToken : String,
        @Path("owner") owner:String,
        @Path("repo") repo:String,
        @Path("type") type : String,
        @Path("number") number: String,
    ): Response<List<Comment>>

    @GET("/search/repositories")
    suspend fun getRepoByQuery(
        @Header("Authorization") typedAccessToken : String,
        @Query("q") query:String,
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int
    ): Response<Repo>

    @PATCH("/notifications/threads/{thread_id}")
    suspend fun changeNotificationAsRead(
        @Header("Authorization") typedAccessToken : String,
        @Path("thread_id") threadID: String
    ): Response<String>


}