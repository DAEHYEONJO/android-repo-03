package com.example.gitreposearch.data.notifications

import com.google.gson.annotations.SerializedName

data class Notifications(
    val id : String = "",
    val subject : Subject,
    val repository: Repository,
    @SerializedName("updated_at")
    val updatedAt: String,
    val url : String
) {
    var commentsCounts : String=""
    var number : String = ""
    var threadID : String = ""
}
