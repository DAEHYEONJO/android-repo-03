package com.example.gitreposearch.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.R
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.databinding.ActivityMainBinding
import com.example.gitreposearch.ui.fragments.IssueFragment
import com.example.gitreposearch.ui.fragments.NotificationFragment
import com.example.gitreposearch.utils.Constants
import com.example.gitreposearch.viewmodel.CustomViewModelFactory
import com.example.gitreposearch.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        CustomViewModelFactory(GlobalApplication.githubApiRepository)
    }
    private lateinit var token: Token

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        getToken()
        initToggleTabButton()
        initToggleTabObserver()
    }

    private fun getToken(){
        mainViewModel.token.value = intent.getSerializableExtra("token") as Token
    }


    private fun initToggleTabButton(){
        with(binding){
            mainIssueTabBtn.setOnClickListener {
                mainViewModel.changeState("Issue")
            }
            mainNotificationTabBtn.setOnClickListener {
                mainViewModel.changeState("Notifications")
            }
        }
    }

    private fun initToggleTabObserver(){
        mainViewModel.currentTabState.observe(this, Observer{ newState ->
            when(newState){
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
        })
    }

    private fun setFrag(state: String) {
        with(supportFragmentManager) {
            when(state) {
                "Issue" -> {
                    commit{replace<IssueFragment>(R.id.main_hostFrag)}

                }
                "Notifications" -> {
                    commit{replace<NotificationFragment>(R.id.main_hostFrag)}
                }

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_appbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.mainAppbar_search -> {
            // Todo : SearchActivity 로 화면전환
            true
        }

        R.id.mainAppbar_profile -> {
            startActivity(Intent(this, ProfileActivity::class.java).apply {
                putExtra("token", intent.getSerializableExtra("token"))
            })

            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
    
}