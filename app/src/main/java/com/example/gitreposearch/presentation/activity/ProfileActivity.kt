package com.example.gitreposearch.presentation.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.R
import com.example.gitreposearch.databinding.ActivityProfileBinding
import com.example.gitreposearch.presentation.viewmodel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    private val binding: ActivityProfileBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_profile)
    }
    private val profileViewModel: ProfileViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(GlobalApplication.githubApiRxRepository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding){
            lifecycleOwner = this@ProfileActivity
            vm = profileViewModel
            setContentView(root)
        }
        initAppBar()
    }

    private fun initAppBar() {
        this.title = ""
        with(binding.appBarProfile) {
            appBarTitleTv.text = resources.getString(R.string.app_bar_profile)
            appBarBackBtn.setOnClickListener { finish() }
        }
    }

}