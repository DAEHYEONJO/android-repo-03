package com.example.gitreposearch.utils

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WrapContentLinearLayoutManager : LinearLayoutManager{
    constructor(context: Context?) : super(context)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try{
            super.onLayoutChildren(recycler, state)
        }
        catch (e : IndexOutOfBoundsException){
            Log.e("WrapContentListener", "onLayoutChildren: $e", )
        }
    }

}