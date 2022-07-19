package com.example.gitreposearch.ui.adapter

import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginBottom
import com.example.gitreposearch.R
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
        val row = inflater.inflate(R.layout.tv_spinner_issue_option, parent, false)
        val tvSpinnerOption =row.findViewById<TextView>(R.id.tv_spinner_selected_option)
        tvSpinnerOption.text =  list[position]
        return row
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.spinner_issue_row, parent, false)
        row.findViewById<TextView>(R.id.tv_spinner_option).text = list[position]
        if(mainViewModel.issueState.value == list[position]){
            row.findViewById<ImageView>(R.id.iv_spinner_isSelected).visibility = VISIBLE
        }
        else{
            row.findViewById<ImageView>(R.id.iv_spinner_isSelected).visibility = GONE
        }
        return row
    }
}