package com.example.gitreposearch.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gitreposearch.R
import com.example.gitreposearch.databinding.ActivityLoginBinding
import com.example.gitreposearch.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    private val binding : ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


}