package com.example.gitreposearch.domain.model.notifications

enum class Type(val type : String) {
    PullRequest("pulls"),
    Issue("issues")
}