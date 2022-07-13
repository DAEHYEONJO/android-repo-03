package com.example.gitreposearch.data

import com.google.gson.annotations.SerializedName

// login: String?
// name: String?
// avatar_url: String?
// bio: String?
//--------
//location: String?
//blog: String?
//email: String?
//followers, following (Int)
//--------
//public_repos + total_private_repos (Int)
data class UserInfo(
    @SerializedName("login") val login: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("blog") val blog: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("followers") val followers: Int,
    @SerializedName("following") val following: Int,
    @SerializedName("public_repos") val publicRepos: Int,
    @SerializedName("total_private_repos") val totalPrivateRepos: Int
)
