package com.example.gitreposearch.ui.activity

import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.gitreposearch.R
import com.example.gitreposearch.data.UserInfo
import com.example.gitreposearch.databinding.ActivityProfileBinding
import com.example.gitreposearch.ui.viewmodel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    private val binding: ActivityProfileBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_profile)
    }
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding){
            lifecycleOwner = this@ProfileActivity
            loginViewModel = profileViewModel
            setContentView(root)
        }
        initUserInfo()
        initAppBar()
    }

    private fun initUserInfo() {
        profileViewModel.userInfo.value = intent.getSerializableExtra("userInfo") as UserInfo
    }

    private fun initAppBar() {
        this.title = ""
        with(binding.appBarProfile) {
            appBarTitleTv.text = resources.getString(R.string.app_bar_profile)
            appBarBackBtn.setOnClickListener { finish() }
        }
    }

}