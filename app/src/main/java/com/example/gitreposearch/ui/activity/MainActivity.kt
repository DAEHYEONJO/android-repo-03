package com.example.gitreposearch.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.R
import com.example.gitreposearch.adapter.IssueListRecyclerViewAdapter
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.databinding.ActivityMainBinding
import com.example.gitreposearch.ui.fragments.IssueFragment
import com.example.gitreposearch.ui.fragments.NotificationFragment
import com.example.gitreposearch.viewmodel.CustomViewModelFactory
import com.example.gitreposearch.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding



    private val mainViewModel: MainViewModel by viewModels {
        CustomViewModelFactory(GlobalApplication.githubApiRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getToken()
        mainViewModel.getNotificationList(mainViewModel.token.value!!, true)
        initAppBarButton()
        initObserver()
        initToggleTabButton()
    }


    private fun initAppBarButton() {
        with(binding) {
            mainAppbarProfileBtn.setOnClickListener {
                startActivity(Intent(this@MainActivity, ProfileActivity::class.java).apply {
                    putExtra("userInfo", mainViewModel.userInfo.value)
                })
            }
            mainAppbarSearchBtn.setOnClickListener {
                Log.d("search btn", "clicked")
            }
        }
    }


    private fun initObserver() {
        with(mainViewModel) {
            token.observe(this@MainActivity) { token ->
                getUserInfo(token)
            }
            userInfo.observe(this@MainActivity) { userInfo ->
                Glide.with(this@MainActivity).load(userInfo.avatarUrl)
                    .circleCrop()
                    .into(binding.mainAppbarProfileBtn)
            }
            currentTabState.observe(this@MainActivity) { newState ->
                with(binding) {
                    when (newState) {
                        "Issue" -> {
                            mainIssueTabBtn.isChecked = true
                            mainNotificationTabBtn.isChecked = false
                        }
                        "Notifications" -> {
                            mainIssueTabBtn.isChecked = false
                            mainNotificationTabBtn.isChecked = true
                        }
                    }

                    setFrag(newState)
                }
            }
        }
    }

    private fun getToken() {
        mainViewModel.token.value = intent.getSerializableExtra("token") as Token
    }


    private fun initToggleTabButton() {
        with(binding) {
            mainIssueTabBtn.setOnClickListener {
                mainViewModel.changeState("Issue")
            }
            mainNotificationTabBtn.setOnClickListener {
                mainViewModel.changeState("Notifications")
            }
        }
    }

    private fun setFrag(state: String) {
        with(supportFragmentManager) {
            val fragment = findFragmentByTag(state) // 현재 state 로 저장된 프래그먼트가 있는지 찾아봄
            when (state) {
                "Issue" -> {
                    if(fragment == null){ // 최초로 생성되는 프래그먼트라면
                        commit { replace<IssueFragment>(R.id.main_hostFrag, state) } // 최초생성
                    }
                    else {
                        commit { replace(R.id.main_hostFrag, fragment) } // 기존에 있던 놈으로
                    }
                }
                "Notifications" -> {
                    val fragment = supportFragmentManager.findFragmentByTag(state)
                    if(fragment == null){
                        commit { replace<NotificationFragment>(R.id.main_hostFrag, state) }
                    }
                    else {
                        commit { replace(R.id.main_hostFrag, fragment) }
                    }
                }
            }
        }
    }


}