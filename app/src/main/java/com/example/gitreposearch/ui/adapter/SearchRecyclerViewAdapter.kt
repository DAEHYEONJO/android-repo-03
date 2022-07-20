package com.example.gitreposearch.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitreposearch.R
import com.example.gitreposearch.data.Repo
import com.example.gitreposearch.databinding.RvSearchRowBinding
import com.example.gitreposearch.utils.ConvertUtils
import java.util.*

class SearchRecyclerViewAdapter :
    PagingDataAdapter<Repo.Item, SearchRecyclerViewAdapter.ViewHolder>(diffUtil) {

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Repo.Item>(){
            override fun areItemsTheSame(oldItem: Repo.Item, newItem: Repo.Item): Boolean {
                return oldItem.owner == newItem.owner
            }

            override fun areContentsTheSame(oldItem: Repo.Item, newItem: Repo.Item): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(private val binding: RvSearchRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Repo.Item) {
            with(binding) {
                this.item = item
                executePendingBindings()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvSearchRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }


}