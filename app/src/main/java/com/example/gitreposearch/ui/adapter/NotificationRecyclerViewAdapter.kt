package com.example.gitreposearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitreposearch.data.notifications.Notifications
import com.example.gitreposearch.databinding.RvNotificationRowBinding
import java.text.SimpleDateFormat
import java.util.*

class NotificationRecyclerViewAdapter(private var dataSet: MutableList<Notifications> = mutableListOf())
    : RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder>(){

    class ViewHolder(private val binding : RvNotificationRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : Notifications){
            with(binding){
                notification = item
                executePendingBindings() // 강제 결합시키기
            }
        }
    }

    fun setData(data : MutableList<Notifications>){
        dataSet = data
        notifyDataSetChanged()
    }

    fun removeData(position: Int) {
        dataSet.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvNotificationRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }


}