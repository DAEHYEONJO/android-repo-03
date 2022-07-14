package com.example.gitreposearch.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gitreposearch.R
import com.example.gitreposearch.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private val binding: ActivitySearchBinding by lazy{ActivitySearchBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}