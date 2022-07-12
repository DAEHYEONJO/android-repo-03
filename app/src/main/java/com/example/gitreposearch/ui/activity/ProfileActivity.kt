package com.example.gitreposearch.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gitreposearch.R
import com.example.gitreposearch.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAppBar()
    }

    private fun initAppBar() {
        this.title = ""
        with(binding.profileAppBar) {
            setSupportActionBar(searchProfileToolBar)
            appBarTitleTv.text = resources.getString(R.string.profile)
        }
    }

}