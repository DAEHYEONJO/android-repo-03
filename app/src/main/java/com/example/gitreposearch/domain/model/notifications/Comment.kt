package com.example.gitreposearch.domain.model.notifications

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("url") val url : String

)
