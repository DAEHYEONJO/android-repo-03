package com.example.gitreposearch.domain.model.notifications

import com.google.gson.annotations.SerializedName

data class Owner(
    @SerializedName("avatar_url")
    val avatarUrl : String,
    val login : String
)
