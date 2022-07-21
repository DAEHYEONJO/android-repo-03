package com.example.gitreposearch.utils

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.example.gitreposearch.data.notifications.Notifications

class NotificationDiffCallBack
    : DiffUtil.ItemCallback<Notifications>() {
    override fun areItemsTheSame(oldItem: Notifications, newItem: Notifications): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Notifications, newItem: Notifications): Boolean {
        return oldItem == newItem
    }


}