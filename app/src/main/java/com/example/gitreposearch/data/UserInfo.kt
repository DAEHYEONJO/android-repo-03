package com.example.gitreposearch.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

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
): Serializable {
    var starredCount = 0
}
