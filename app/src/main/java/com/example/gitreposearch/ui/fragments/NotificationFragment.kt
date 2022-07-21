package com.example.gitreposearch.ui.fragments

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.data.notifications.Notifications
import com.example.gitreposearch.databinding.FragmentNotificationBinding
import com.example.gitreposearch.ui.adapter.NotificationAdapter
import com.example.gitreposearch.ui.viewmodel.MainViewModel
import com.example.gitreposearch.utils.SwipeHelperCallback
import com.example.gitreposearch.utils.WrapContentLinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NotificationFragment : Fragment() {

    val TAG = "NotificationFragment"

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        initNotificationRecyclerView()
        initRefreshListener()
        initSwipeListener()
        initObserver()
    }

    private fun initObserver() {
        mainViewModel.userNotificationList.observe(viewLifecycleOwner){
            notificationAdapter.submitList(mainViewModel.userNotificationList.value)
        }
        mainViewModel.commentInfo.observe(viewLifecycleOwner){ commentInfo ->
            initNotificationRecyclerView()
            if(binding.layoutRefresh.isRefreshing){
                binding.layoutRefresh.isRefreshing = false
            }
        }
    }

    private fun initSwipeListener() {
        val swipeHelperCallback =
            SwipeHelperCallback(requireContext(), mainViewModel, notificationAdapter).apply {
            }
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rcvNotificationList)
    }

    private fun initRefreshListener() {
        binding.layoutRefresh.setOnRefreshListener { // 새로고침 이벤트 리스너
            binding.layoutRefresh.isRefreshing = true // 새로고침 로딩 보여주기
            notificationAdapter.removeAll() // 어댑터 목록 지우기

            // 데이터 요청
            mainViewModel.getNotificationList(GlobalApplication.getInstance().getTypedAccessToken().toString())

        }
    }

    private fun initNotificationRecyclerView() {
        notificationAdapter = NotificationAdapter(mainViewModel)
        with(binding) {
            rcvNotificationList.layoutManager = LinearLayoutManager(activity)
            notificationAdapter.submitList(mainViewModel.userNotificationList.value)
            rcvNotificationList.adapter = notificationAdapter
        }
    }
}