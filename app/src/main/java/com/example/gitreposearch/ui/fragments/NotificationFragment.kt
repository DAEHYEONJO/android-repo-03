package com.example.gitreposearch.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.gitreposearch.databinding.FragmentNotificationBinding
import com.example.gitreposearch.viewmodel.MainViewModel


class NotificationFragment : Fragment() {

    val TAG = "NotificationFragment"
    private var binding : FragmentNotificationBinding? = null
    private val mainViewModel : MainViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentNotificationBinding.inflate(inflater,container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getUserNotificationList()
        initObserve()

    }

    private fun getUserNotificationList() {
        val token = mainViewModel.token.value
        if(token != null){
            mainViewModel.getNotificationList(token, true)
        }
    }


    private fun initObserve() {
        mainViewModel.userNotificationList.observe(viewLifecycleOwner){
            Log.d(TAG, "initObserve: ")
        }
    }
    /*
    <issue 다시 콜하는 부분>
    1. token 이 observe 됐을 때
    2. issue state (all, closed, open) 가 observe 되었을 때

    <Notification 호출되는 부분>
    1. Notification 프래그먼트가 호출되었을 때
    2. 새로 알림 처리를 했을 때
     */

}