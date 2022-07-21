package com.example.gitreposearch.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.gitreposearch.R
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