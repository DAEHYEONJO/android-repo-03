package com.example.gitreposearch.ui.adapter

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitreposearch.data.notifications.Notifications
import com.example.gitreposearch.databinding.RvIssueRowBinding
import com.example.gitreposearch.databinding.RvNotificationRowBinding
import com.example.gitreposearch.ui.viewmodel.MainViewModel
import com.example.gitreposearch.utils.NotificationDiffCallBack

import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(private val mainViewModel : MainViewModel) :
    ListAdapter<Notifications, NotificationAdapter.ViewHolder>(NotificationDiffCallBack()) {
    class ViewHolder(private val binding: RvNotificationRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Notifications) {
            with(binding) {
                notification = item
                setOrganizationImage(item.repository.owner.avatar_url)
                executePendingBindings() // 강제 결합시키기
                setUpdatedTime(item.updated_at)
            }
        }

        private fun setOrganizationImage(avatarUrl: String) {
            Glide.with(binding.ivNotificationOrganizationImage.context).load(avatarUrl)
                .circleCrop()
                .into(binding.ivNotificationOrganizationImage)
        }

        private fun setUpdatedTime(updated_at: String) {
            if(updated_at.length > 10){
                val updateTime =
                    updated_at.substring(0 until 10) + " " + updated_at.substring(11 until 19)
                val sf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                val date = sf.parse(updateTime)
                val currentTime = Calendar.getInstance()
                val timeDiff = (currentTime.time.time - date.time) / (60 * 60 * 24 * 1000)
                with(binding.tvNotificationDate) {
                    text = if (timeDiff > 30) {
                        (timeDiff / 30).toString() + "달 전"
                    } else {
                        timeDiff.toString() + "일 전"
                    }

                }
            }
            else{
                Log.d("time 이상", "setUpdatedTime: $updated_at")
            }
        }
    }

    fun removeAll(){
        val tempList = currentList.toMutableList()
        tempList.clear()
        submitList(tempList)
    }

    fun removeData(position: Int) {
        val tempList = currentList.toMutableList()
        if (position < currentList.size) {
            tempList.removeAt(position)
            mainViewModel.userNotificationList.removeAt(position)
        }
        submitList(tempList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvNotificationRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(holder.adapterPosition)
        holder.bind(item)
    }


}