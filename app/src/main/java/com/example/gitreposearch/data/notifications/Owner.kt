package com.example.gitreposearch.data.notifications

import com.google.gson.annotations.SerializedName

data class Owner(
    @SerializedName("avatar_url")
    val avatarUrl : String,
    val login : String
)
