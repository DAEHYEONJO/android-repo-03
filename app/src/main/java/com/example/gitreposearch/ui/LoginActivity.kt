package com.example.gitreposearch.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.gitreposearch.BuildConfig
import com.example.gitreposearch.R
import com.example.gitreposearch.databinding.ActivityLoginBinding
import com.example.gitreposearch.utils.Constants

class LoginActivity : AppCompatActivity() {
    
    companion object{
        const val TAG = "LoginActivity"
    }

    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initLayout()
    }

    private fun initLayout() {
        binding.run {
            loginBtn.setOnClickListener {
                login()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val uri = intent?.data
        uri?.let { 
            val code = uri.getQueryParameter("code")
            code?.let {
                Log.e(TAG, "onResume: success $code", )
                Log.e(TAG, "onResume: success ${uri.query}", )
                Log.e(TAG, "onResume: success ${uri.userInfo}", )
                Toast.makeText(this@LoginActivity, "success! $code", Toast.LENGTH_SHORT).show()
            }?:uri.getQueryParameter("error")?.let {
                Log.e(TAG, "onResume: error ${it}", )
                Toast.makeText(this@LoginActivity, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(
            "${Constants.oauthLoginUrl}?client_id=${resources.getString(R.string.client_id)}&scope=user+repo"
        ))
        startActivity(intent)

    }
}