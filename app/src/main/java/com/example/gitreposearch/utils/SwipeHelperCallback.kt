package com.example.gitreposearch.utils

import android.graphics.Canvas
import android.icu.lang.UCharacter.IndicPositionalCategory.LEFT
import android.util.Log
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.getTag
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags
import androidx.recyclerview.widget.RecyclerView
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.ui.adapter.NotificationAdapter
import com.example.gitreposearch.ui.viewmodel.MainViewModel
import kotlin.math.abs

class SwipeHelperCallback(
    private val mainViewModel: MainViewModel,
    private val adapter: NotificationAdapter
) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val item = adapter.currentList[position]
        mainViewModel.changeNotificationAsRead(item.threadID)
        adapter.removeData(position)
    }
}