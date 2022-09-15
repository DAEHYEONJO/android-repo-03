package com.example.gitreposearch.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.gitreposearch.R
import com.example.gitreposearch.domain.model.ProfileInfo
import com.example.gitreposearch.presentation.UiState
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("app:setImageUrl")
fun ImageView.setImageUrl(stringUrl: String) {
    Glide.with(this.context)
        .load(Uri.parse(stringUrl))
        .circleCrop()
        .into(this)
}

@BindingAdapter("app:setPaintFlag")
fun TextView.setPaintFlag(paintFlag: Int) {
    this.paintFlags = paintFlag
}

@BindingAdapter("app:setBackgroundColorByString")
fun ImageView.setBackgroundColorByString(stringColor: String) {
    this.setBackgroundColor(Color.parseColor(stringColor))
}

@BindingAdapter("app:setRepositoryUrl")
fun TextView.setRepositoryUrl(repoUrl: String) {
    val baseUrl = Constants.githubBaseUrl + "repos/"
    text = repoUrl.substring(baseUrl.length until repoUrl.length)
}

@BindingAdapter("app:setIssueStateImage")
fun ImageView.setIssueStateImage(state: String) {
    when (state) {
        "open" -> {
            setImageResource(R.drawable.ic_issue_open)
        }
        "closed" -> {
            setImageResource(R.drawable.ic_issue_closed)
        }
    }
}

@SuppressLint("SetTextI18n", "SimpleDateFormat")
@BindingAdapter("app:setUpdatedTime")
fun TextView.setUpdatedTime(updatedAt: String) {
    val updateTime = updatedAt.substring(0 until 10) + " " + updatedAt.substring(11 until 19)
    val sf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    val date = sf.parse(updateTime)
    val currentTime = Calendar.getInstance()
    val timeDiff = (currentTime.time.time - date.time) / (60 * 60 * 24 * 1000)
    text = if (timeDiff > 30) {
        (timeDiff / 30).toString() + "달 전"
    } else {
        timeDiff.toString() + "일 전"
    }

}

@BindingAdapter("app:setUiStateImageUrl")
fun ImageView.setUiStateImageUrl(uiState: UiState){
    if (uiState is UiState.Success<*>){
        val userInfo = (uiState as UiState.Success<ProfileInfo>).data
        setImageUrl(userInfo.avatarUrl!!)
    }
}

@BindingAdapter("app:setUiStateProfileLogin")
fun TextView.setUiStateProfileLogin(uiState: UiState){
    if (uiState is UiState.Success<*>) {
        val userInfo = (uiState as UiState.Success<ProfileInfo>).data
        text = userInfo.login
    }
}

@BindingAdapter("app:setUiStateProfileName")
fun TextView.setUiStateProfileName(uiState: UiState){
    if (uiState is UiState.Success<*>) {
        val userInfo = (uiState as UiState.Success<ProfileInfo>).data
        text = userInfo.name
    }
}

@BindingAdapter("app:setUiStateProfileBio")
fun TextView.setUiStateProfileBio(uiState: UiState){
    if (uiState is UiState.Success<*>) {
        val userInfo = (uiState as UiState.Success<ProfileInfo>).data
        text = userInfo.bio
    }
}

@BindingAdapter("app:setUiStateProfileLocation")
fun TextView.setUiStateProfileLocation(uiState: UiState){
    if (uiState is UiState.Success<*>) {
        val userInfo = (uiState as UiState.Success<ProfileInfo>).data
        text = userInfo.location
    }
}

@BindingAdapter("app:setUiStateProfileBlog")
fun TextView.setUiStateProfileBlog(uiState: UiState){
    if (uiState is UiState.Success<*>) {
        val userInfo = (uiState as UiState.Success<ProfileInfo>).data
        text = userInfo.blog
    }
}

@BindingAdapter("app:setUiStateProfileEmail")
fun TextView.setUiStateProfileEmail(uiState: UiState){
    if (uiState is UiState.Success<*>) {
        val userInfo = (uiState as UiState.Success<ProfileInfo>).data
        text = userInfo.email
    }
}

@BindingAdapter("app:setUiStateProfileFollowers")
fun TextView.setUiStateProfileFollowers(uiState: UiState){
    if (uiState is UiState.Success<*>) {
        val userInfo = (uiState as UiState.Success<ProfileInfo>).data
        text = userInfo.followers
    }
}

@BindingAdapter("app:setUiStateProfileFollowing")
fun TextView.setUiStateProfileFollowing(uiState: UiState){
    if (uiState is UiState.Success<*>) {
        val userInfo = (uiState as UiState.Success<ProfileInfo>).data
        text = userInfo.following
    }
}

@BindingAdapter("app:setUiStateProfileRepositoryCount")
fun TextView.setUiStateProfileRepositoryCount(uiState: UiState){
    if (uiState is UiState.Success<*>) {
        val userInfo = (uiState as UiState.Success<ProfileInfo>).data
        text = userInfo.repositoryCount
    }
}

@BindingAdapter("app:setUiStateProfileStarredCount")
fun TextView.setUiStateProfileStarredCount(uiState: UiState){
    if (uiState is UiState.Success<*>) {
        val userInfo = (uiState as UiState.Success<ProfileInfo>).data
        text = userInfo.starredCount
    }
}

@BindingAdapter("app:setUiStateProfileVisibility")
fun View.setUiStateProfileVisibility(uiState: UiState){
    isVisible = uiState is UiState.Success<*>
}

@BindingAdapter("app:setUiStateProfileProgressVisibility")
fun View.setUiStateProfileProgressVisibility(uiState: UiState){
    isVisible = uiState is UiState.Loading
}