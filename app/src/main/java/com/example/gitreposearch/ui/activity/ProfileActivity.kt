package com.example.gitreposearch.ui.activity

import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.R
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.data.UserInfo
import com.example.gitreposearch.databinding.ActivityProfileBinding
import com.example.gitreposearch.viewmodel.CustomViewModelFactory
import com.example.gitreposearch.viewmodel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(
            layoutInflater
        )
    }
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var token: Token

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initUserInfo()
        initAppBar()
        initObserver()
    }

    private fun initUserInfo() {
        profileViewModel.userInfo.value = intent.getSerializableExtra("userInfo") as UserInfo
    }

    private fun initObserver() {
        profileViewModel.userInfo.observe(this@ProfileActivity) { userInfo ->
            with(binding) {
                Glide.with(this@ProfileActivity)
                    .load(Uri.parse(userInfo.avatarUrl))
                    .circleCrop()
                    .into(profileImg)
                profileLoginTv.text = userInfo.login ?: "Empty"
                profileNameTv.text = userInfo.name ?: "Empty"
                profileBioTv.text = userInfo.bio ?: "Empty"
                profileLocationTv.text = userInfo.location ?: "Empty"
                with(profileBlogTv) {
                    text = userInfo.blog ?: "Empty"
                    paintFlags = Paint.UNDERLINE_TEXT_FLAG
                }
                with(profileEmailTv) {
                    text = userInfo.email ?: "Empty"
                    paintFlags = Paint.UNDERLINE_TEXT_FLAG
                }
                profileFollowersTv.text = userInfo.followers.toString()
                profileFollowingTv.text = userInfo.following.toString()
                profileRepositoryCountTv.text =
                    (userInfo.publicRepos + userInfo.totalPrivateRepos).toString()
            }
        }
    }


    private fun initAppBar() {
        this.title = ""
        with(binding.profileAppBar) {
            setSupportActionBar(searchProfileToolBar)
            appBarTitleTv.text = resources.getString(R.string.profile)
        }
    }

}