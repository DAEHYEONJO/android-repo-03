package com.example.gitreposearch.data

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("scope") val scope: String,
    @SerializedName("token_type") val tokenType: String
)