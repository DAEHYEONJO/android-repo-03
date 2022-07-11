package com.example.gitreposearch.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.gitreposearch.R
import com.example.gitreposearch.databinding.ActivityLoginBinding
import com.example.gitreposearch.network.GithubApiImpl
import com.example.gitreposearch.utils.Constants
import kotlinx.coroutines.launch

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
                testGetToken(it)
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

    //Todo 삭제할 함수 -> Repository and ViewModel 로 이동
    private fun testGetToken(code: String){
        lifecycleScope.launch {
            val response = GithubApiImpl.githubApi.getAccessToken(
                clientId = resources.getString(R.string.client_id),
                clientSecret = resources.getString(R.string.client_secret),
                code = code
            )
            Log.e(TAG, "testGetToken: ${response.code()}", )
            Log.e(TAG, "testGetToken: ${response.body()}", )
            Log.e(TAG, "testGetToken: ${response.errorBody()}", )
        }

    }

    private fun login(){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(
            "${Constants.oauthLoginUrl}?client_id=${resources.getString(R.string.client_id)}&scope=user+repo"
        ))
        startActivity(intent)

    }
}