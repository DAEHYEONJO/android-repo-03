package com.example.gitreposearch.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitreposearch.databinding.FragmentNotificationBinding
import com.example.gitreposearch.utils.SwipeHelperCallback
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.data.notifications.Notifications
import com.example.gitreposearch.ui.adapter.NotificationRecyclerViewAdapter
import com.example.gitreposearch.ui.viewmodel.MainViewModel


class NotificationFragment : Fragment() {
    private var binding: FragmentNotificationBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var notificationRecyclerViewAdapter: NotificationRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        initNotificationRecyclerView()
        initRefreshListener()
        initSwipeListener()
        initObserve()
    }

    private fun initSwipeListener() {
        val swipeHelperCallback = SwipeHelperCallback(mainViewModel, notificationRecyclerViewAdapter).apply{
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding?.rcvNotificationList)
    }

    private fun initRefreshListener() {
        binding!!.layoutRefresh.setOnRefreshListener {
            getUserNotificationList()
        }
    }

    private fun showLoading() {
        with(binding!!){
            progressBarLoading.isGone = false
            tvLoading.isGone = false
        }
    }

    private fun hideLoading() {
        with(binding!!){
            progressBarLoading.isGone=true
            tvLoading.isGone = true
        }
    }

    private fun initNotificationRecyclerView() {
        notificationRecyclerViewAdapter = NotificationRecyclerViewAdapter()
        with(binding!!) {
            rcvNotificationList.layoutManager = LinearLayoutManager(activity)
            rcvNotificationList.adapter = notificationRecyclerViewAdapter
        }
    }

    private fun getUserNotificationList() {
        mainViewModel.getNotificationList(GlobalApplication.getInstance().getTypedAccessToken()!!)
    }

    private fun initObserve() {
        mainViewModel.userNotificationList.observe(viewLifecycleOwner) { notificationList ->
            if(binding!!.layoutRefresh.isRefreshing){
                binding!!.layoutRefresh.isRefreshing = false
            }
            hideLoading()
            notificationRecyclerViewAdapter.setData(notificationList as MutableList<Notifications>)
        }
    }
}