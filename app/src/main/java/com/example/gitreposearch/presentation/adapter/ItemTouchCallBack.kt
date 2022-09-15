package com.example.gitreposearch.presentation.adapter

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView

interface ItemTouchCallBack {
    fun onSwiped(position: Int)
    fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    )
}