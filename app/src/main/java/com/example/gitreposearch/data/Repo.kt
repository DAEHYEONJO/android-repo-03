package com.example.gitreposearch.data

import com.google.gson.annotations.SerializedName

data class Repo(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("total_count")
    val totalCount: Int
) {

    data class Item(

        @SerializedName("description")
        val description: String,
        @SerializedName("language")
        val language: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("owner")
        val owner: Owner,
        @SerializedName("stargazers_count")
        val stargazersCount: Int
    ) {
        lateinit var colorString: String
        lateinit var stargazersCountString: String
        data class Owner(
            @SerializedName("avatar_url")
            val avatarUrl: String,
            @SerializedName("login")
            val login: String
        )

    }
}