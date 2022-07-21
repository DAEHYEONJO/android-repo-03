package com.example.gitreposearch.data

import com.google.gson.annotations.SerializedName

data class Issue(
    @SerializedName("repository_url") val repositoryUrl: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("number") val number: String?,
    @SerializedName("updated_at") val updatedAt: String?
)
