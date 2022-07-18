package com.example.gitreposearch.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitreposearch.data.notifications.Notifications
import com.example.gitreposearch.databinding.ItemNotificationListBinding
import java.text.SimpleDateFormat
import java.util.*

class NotificationRecyclerViewAdapter()  : RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder>() {
    private var dataSet =listOf<Notifications>()

    class ViewHolder(private val binding : ItemNotificationListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : Notifications){
            with(binding){
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

        private fun setUpdatedTime(updated_at : String) {
            val updateTime = updated_at.substring(0 until 10) +" " + updated_at.substring(11 until 19)
            val sf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val date = sf.parse(updateTime)
            val currentTime = Calendar.getInstance()
            val timeDiff = (currentTime.time.time - date.time) / (60*60*24*1000)
            with(binding.tvNotificationDate) {
                text = if(timeDiff > 30){
                    (timeDiff /30 ).toString() + "달 전"
                } else {
                    timeDiff.toString() +"일 전"
                }

            }
        }
    }
    fun setData(data : List<Notifications>){
        dataSet = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNotificationListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}