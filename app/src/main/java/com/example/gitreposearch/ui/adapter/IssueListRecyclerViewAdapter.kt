package com.example.gitreposearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gitreposearch.data.Issue
import com.example.gitreposearch.databinding.RvIssueRowBinding

class IssueListRecyclerViewAdapter(private var dataSet: MutableList<Issue> = mutableListOf<Issue>()) : RecyclerView.Adapter<IssueListRecyclerViewAdapter.ViewHolder>() {


    class ViewHolder(private val binding : RvIssueRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Issue){
            with(binding){
                issue = item
                executePendingBindings() // 강제 결합시키기
            }
        }
    }
    fun setData(data: MutableList<Issue>){
        dataSet = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvIssueRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}