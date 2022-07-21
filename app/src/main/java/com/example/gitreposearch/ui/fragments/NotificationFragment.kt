package com.example.gitreposearch.ui.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.R
import com.example.gitreposearch.databinding.FragmentNotificationBinding
import com.example.gitreposearch.ui.adapter.ItemTouchCallBack
import com.example.gitreposearch.ui.adapter.NotificationRecyclerViewAdapter
import com.example.gitreposearch.ui.viewmodel.MainViewModel
import com.example.gitreposearch.utils.SwipeHelperCallback
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class NotificationFragment : Fragment() {

    val TAG = "NotificationFragment"

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private val notificationAdapter: NotificationRecyclerViewAdapter by lazy { NotificationRecyclerViewAdapter(mainViewModel.userNotificationList.value!!) }

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

        initRefreshListener()
        initSwipeListener()
        initObserver()
    }

    private fun initObserver() {
        mainViewModel.commentInfo.observe(viewLifecycleOwner){ commentInfo ->
            initNotificationRecyclerView()
            notificationAdapter.setData(mainViewModel.userNotificationList.value!!)
            if(binding.layoutRefresh.isRefreshing){
                binding.layoutRefresh.isRefreshing = false
            }
        }
    }

    private fun initSwipeListener() {
        val swipeHelperCallback = SwipeHelperCallback(itemTouchCallBack = object : ItemTouchCallBack{
            override fun onSwiped(position: Int) {
                mainViewModel.changeNotificationAsRead(position)
                notificationAdapter.removeData(position)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    .addActionIcon(R.drawable.bg_notification_check)
                    .create()
                    .decorate()
            }
        })
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rcvNotificationList)
    }

    private fun initRefreshListener() {
        binding.layoutRefresh.setOnRefreshListener { // 새로고침 이벤트 리스너
            binding.layoutRefresh.isRefreshing = true // 새로고침 로딩 보여주기
            // 데이터 요청
            mainViewModel.getNotificationList(GlobalApplication.getInstance().getTypedAccessToken().toString())

        }
    }

    private fun initNotificationRecyclerView() {
        with(binding) {
            rcvNotificationList.adapter = notificationAdapter
        }
    }
}