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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getToken()
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
                    .into(binding.mainAppbarProfileBtn);
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
            Log.e("IssueFragment", "setFrag: ${this.fragments.toList()}", )
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