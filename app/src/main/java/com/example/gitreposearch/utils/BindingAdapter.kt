package com.example.gitreposearch.utils

import android.graphics.Color
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("app:setImageUrl")
fun ImageView.setImageUrl(stringUrl: String) {
    Glide.with(this.context)
        .load(Uri.parse(stringUrl))
        .circleCrop()
        .into(this)
}

@BindingAdapter("app:setPaintFlag")
fun TextView.setPaintFlag(paintFlag: Int){
    this.paintFlags = paintFlag
}

@BindingAdapter("app:setBackgroundColorByString")
fun ImageView.setBackgroundColorByString(stringColor: String){
    this.setBackgroundColor(Color.parseColor(stringColor))
}
