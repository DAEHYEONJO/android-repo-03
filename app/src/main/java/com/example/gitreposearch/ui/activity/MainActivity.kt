package com.example.gitreposearch.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import androidx.activity.viewModels
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
    private lateinit var token: Token

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getToken()
        initAppBarButton()
        initObserver()
        initToggleTabButton()
        initToggleTabObserver()
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
                mainViewModel.getUserIssueList(token, mainViewModel.issueState.value.toString())
                //getIssueList()
            }
            userInfo.observe(this@MainActivity) { userInfo ->
                Glide.with(this@MainActivity).load(userInfo.avatarUrl)
                    .into(binding.mainAppbarProfileBtn);
            }
            userIssueList.observe(this@MainActivity) {
                Log.d("IssueList", mainViewModel.userIssueList.value.toString())
            }
        }
    }

    private fun getToken() {
        mainViewModel.token.value = intent.getSerializableExtra("token") as Token
    }

    /*
       private fun getIssueList() {
           mainViewModel.getUserIssueList(token)
       }
     */


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

    private fun initToggleTabObserver() {
        mainViewModel.currentTabState.observe(this) { newState ->
            when (newState) {
                "Issue" -> {
                    binding.mainIssueTabBtn.isChecked = true
                    binding.mainNotificationTabBtn.isChecked = false
                }
                "Notifications" -> {
                    binding.mainIssueTabBtn.isChecked = false
                    binding.mainNotificationTabBtn.isChecked = true
                }
            }
            setFrag(newState)
        }
    }

    private fun setFrag(state: String) {
        with(supportFragmentManager) {
            when (state) {
                "Issue" -> {
                    commit { replace<IssueFragment>(R.id.main_hostFrag) }

                }
                "Notifications" -> {
                    commit { replace<NotificationFragment>(R.id.main_hostFrag) }
                }

            }
        }
    }

}