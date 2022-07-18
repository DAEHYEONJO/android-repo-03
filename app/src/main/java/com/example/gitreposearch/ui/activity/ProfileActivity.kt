package com.example.gitreposearch.ui.activity

import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.gitreposearch.R
import com.example.gitreposearch.data.UserInfo
import com.example.gitreposearch.databinding.ActivityProfileBinding
import com.example.gitreposearch.ui.viewmodel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(
            layoutInflater
        )
    }
    private val profileViewModel: ProfileViewModel by viewModels()

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
                    .into(ivProfile)
                tvProfileLogin.text = userInfo.login ?: "Empty"
                tvProfileName.text = userInfo.name ?: "Empty"
                tvProfileBio.text = userInfo.bio ?: "Empty"
                tvProfileLocation.text = userInfo.location ?: "Empty"
                with(tvProfileBlog) {
                    text = userInfo.blog ?: "Empty"
                    paintFlags = Paint.UNDERLINE_TEXT_FLAG
                }
                with(tvProfileEmail) {
                    text = userInfo.email ?: "Empty"
                    paintFlags = Paint.UNDERLINE_TEXT_FLAG
                }
                tvProfileFollowers.text = userInfo.followers.toString()
                tvProfileFollowing.text = userInfo.following.toString()
                tvProfileRepositoryCount.text =
                    (userInfo.publicRepos + userInfo.totalPrivateRepos).toString()
                tvProfileStarredCount.text = userInfo.starredCount.toString()
            }
        }
    }


    private fun initAppBar() {
        this.title = ""
        with(binding.appBarProfile) {
            //setSupportActionBar(searchProfileToolBar)
            appBarTitleTv.text = resources.getString(R.string.app_bar_profile)
            appBarBackBtn.setOnClickListener { finish() }
        }
    }

}