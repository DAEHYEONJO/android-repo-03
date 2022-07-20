package com.example.gitreposearch.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.R
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.databinding.ActivityMainBinding
import com.example.gitreposearch.ui.fragments.IssueFragment
import com.example.gitreposearch.ui.fragments.NotificationFragment
import com.example.gitreposearch.utils.CustomViewModelFactory
import com.example.gitreposearch.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding



    private val mainViewModel: MainViewModel by viewModels {
        CustomViewModelFactory(GlobalApplication.githubApiRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.getUserInfo(GlobalApplication.getInstance().getTypedAccessToken()!!)
        mainViewModel.getNotificationList(GlobalApplication.getInstance().getTypedAccessToken()!!)
        initAppBarButton()
        initObserver()
        initToggleTabButton()

    }

    private fun initAppBarButton() {
        with(binding) {
            btnMainProfile.setOnClickListener {
                startActivity(Intent(this@MainActivity, ProfileActivity::class.java).apply {
                    putExtra("userInfo", mainViewModel.userInfo.value)
                })
            }
            btnMainSearch.setOnClickListener {
                Log.d("search btn", "clicked")
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
        }
    }


    private fun initObserver() {
        with(mainViewModel) {
            userInfo.observe(this@MainActivity) { userInfo ->
                Glide.with(this@MainActivity).load(userInfo.avatarUrl)
                    .circleCrop()
                    .into(binding.btnMainProfile)
            }
            currentTabState.observe(this@MainActivity) { newState ->
                with(binding) {
                    when (newState) {
                        "Issue" -> {
                            btnMainIssueTab.isChecked = true
                            btnMainNotifiTab.isChecked = false
                        }
                        "Notifications" -> {
                            btnMainIssueTab.isChecked = false
                            btnMainNotifiTab.isChecked = true
                        }
                    }

                    setFrag(newState)
                }
            }
//            userNotificationList.observe(this@MainActivity){
//                getNotifiCommentList(GlobalApplication.getInstance().getTypedAccessToken().toString())
//            }
        }
    }

    private fun initToggleTabButton() {
        with(binding) {
            btnMainIssueTab.setOnClickListener {
                mainViewModel.changeState("Issue")
            }
            btnMainNotifiTab.setOnClickListener {
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
                       commit { replace<IssueFragment>(R.id.layout_hostFrag, state) } // 최초생성
                    }
                    else {
                        commit { replace(R.id.layout_hostFrag, fragment) } // 기존에 있던 놈으로
                    }
                }
                "Notifications" -> {
                    if(fragment == null){
                        commit { replace<NotificationFragment>(R.id.layout_hostFrag, state) }
                    }
                    else {
                        commit { replace(R.id.layout_hostFrag, fragment) }
                    }
                }
            }
        }
    }


}