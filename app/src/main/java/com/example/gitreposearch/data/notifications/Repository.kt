package com.example.gitreposearch.data.notifications

import com.google.gson.annotations.SerializedName

data class Repository(
    val owner: Owner,
    val name : String,
    @SerializedName("full_name")
    val fullName : String
)
