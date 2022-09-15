package com.example.gitreposearch.domain.model

data class ProfileInfo(
    val avatarUrl: String,
    val login: String,
    val name: String,
    val bio: String,
    val location: String,
    val blog: String,
    val email: String,
    val followers: String,
    val following: String,
    val repositoryCount: String,
    val starredCount: String
)
