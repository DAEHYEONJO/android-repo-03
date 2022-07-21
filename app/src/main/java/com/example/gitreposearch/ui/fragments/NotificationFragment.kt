package com.example.gitreposearch.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        startFlow()

    }

    private fun startFlow() {
        lifecycleScope.launch{
            mainViewModel.userNotificationFlow.collectLatest{ // flow 데이터 받아오기
                if(!mainViewModel.userNotificationList.contains(it)){
                    val newList = notificationAdapter.currentList.toMutableList() // List<Notifications> 타입으로 mutable 로 Casting
                    newList.add(it) // flow로 받아온 데이터를 기존에 있던 리스트에 추가
                    notificationAdapter.submitList(newList ) // 데이터   보냄

                }
                if (binding.layoutRefresh.isRefreshing) { // 새로고침 로딩표시 없애주는 조건문
                    binding.layoutRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun initSwipeListener() {
        val swipeHelperCallback =
            SwipeHelperCallback(mainViewModel, notificationAdapter).apply {
            }
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rcvNotificationList)
    }

    private fun initRefreshListener() {
        binding.layoutRefresh.setOnRefreshListener { // 새로고침 이벤트 리스너
            binding.layoutRefresh.isRefreshing = true // 새로고침 로딩 보여주기
            notificationAdapter.removeAll() // 어댑터 목록 지우기

            with(mainViewModel){ //userNotificationList.clear() // mainViewModel 에 보관중이던 데이터 지우기
               getNotificationList(GlobalApplication.getInstance().getTypedAccessToken().toString()) // NotificationList 받아오는 함수 실행
            }
        }
    }

    private fun initNotificationRecyclerView() {
        notificationAdapter = NotificationAdapter(mainViewModel)
        with(binding) {
            rcvNotificationList.layoutManager = WrapContentLinearLayoutManager(context)
            notificationAdapter.submitList(mainViewModel.userNotificationList)
            rcvNotificationList.adapter = notificationAdapter
        }
    }
}