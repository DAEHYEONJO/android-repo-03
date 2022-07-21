package com.example.gitreposearch.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.gitreposearch.databinding.SpinnerIssueRowBinding
import com.example.gitreposearch.databinding.TvSpinnerIssueOptionBinding
import com.example.gitreposearch.ui.viewmodel.MainViewModel

class SpinnerCustomAdapter(private val context: Context, private val mainViewModel : MainViewModel) : BaseAdapter() {

    val list = listOf("Open", "Closed", "All")

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = TvSpinnerIssueOptionBinding.inflate(inflater,parent,false)

        binding.tvSpinnerSelectedOption.text =  list[position]
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val binding = SpinnerIssueRowBinding.inflate(inflater,parent,false)
        binding.tvSpinnerOption.text = list[position]
        if(mainViewModel.issueState.value == list[position]){
            binding.ivSpinnerIsSelected.visibility = VISIBLE
        }
        else{
            binding.ivSpinnerIsSelected.visibility = GONE
        }
        return binding.root
    }
}