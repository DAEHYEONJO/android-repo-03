package com.example.gitreposearch.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.gitreposearch.BuildConfig
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.databinding.ActivityLoginBinding
import com.example.gitreposearch.utils.Constants
import com.example.gitreposearch.utils.CustomViewModelFactory
import com.example.gitreposearch.ui.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    companion object {
        const val TAG = "LoginActivity"
    }

    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val loginViewModel: LoginViewModel by viewModels {
        CustomViewModelFactory(GlobalApplication.githubRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initLayout()
        initObserver()
    }

    private fun initObserver() {
        loginViewModel.token.observe(this@LoginActivity) { token ->
            Log.e(TAG, "initObserver: token: $token", )
            val observedTypedToken = "${token.tokenType} ${token.accessToken}"
            with(GlobalApplication.getInstance()){
                val typedToken = getTypedAccessToken()
                if (typedToken==null || observedTypedToken!=typedToken){
                    putTypedAccessToken(observedTypedToken)
                }
            }
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun initLayout() {
        binding.run {
            btnLogin.setOnClickListener {
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
                loginViewModel.getToken(it)
                Toast.makeText(this@LoginActivity, "success! $code", Toast.LENGTH_SHORT).show()
            } ?: Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_LONG).show()
        }
    }

    private fun login() {
        val intent = Intent(
            Intent.ACTION_VIEW, Uri.parse(
                "${Constants.oauthLoginUrl}?client_id=${BuildConfig.CLIENT_ID}&scope=user+repo"
            )
        )
        startActivity(intent)
    }


}