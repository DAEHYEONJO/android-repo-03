package com.example.gitreposearch.data.notifications

data class Notifications(
    val id : String = "",
    val subject : Subject,
    val repository: Repository,
    val updated_at: String,
) {
    var commentsCounts : String=""
    var number : String = ""
}
