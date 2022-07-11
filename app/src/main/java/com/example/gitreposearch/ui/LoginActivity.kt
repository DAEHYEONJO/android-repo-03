package com.example.gitreposearch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gitreposearch.R
import com.example.gitreposearch.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}