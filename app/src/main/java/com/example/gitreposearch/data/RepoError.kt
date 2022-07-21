package com.example.gitreposearch.data


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

data class RepoError(
    @SerializedName("documentation_url")
    val documentationUrl: String,
    @SerializedName("message")
    val message: String
)