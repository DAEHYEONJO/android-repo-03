package com.example.gitreposearch.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.gitreposearch.BuildConfig
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.R
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
        with(loginViewModel){
            token.observe(this@LoginActivity) { token ->
                Log.e(TAG, "initObserver: token: $token", )
                val observedTypedToken = "${token.tokenType} ${token.accessToken}"
                with(GlobalApplication.getInstance()){
                    val typedToken = getTypedAccessToken()
                    if (typedToken==null || observedTypedToken!=typedToken){
                        putTypedAccessToken(observedTypedToken)
                    }
                }
                startActivity(Intent(this@LoginActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                finish()
            }
            error.observe(this@LoginActivity) { e ->
                Log.e(TAG, "initObserver: e: $e", )
                if (e) {
                    Toast.makeText(this@LoginActivity, resources.getString(R.string.login_fail_msg), Toast.LENGTH_SHORT).show()
                }
            }
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
        val uri = intent.data
        uri?.let {
            val code = uri.getQueryParameter("code")
            code?.let {
                loginViewModel.getToken(it)
            }
        } ?: if (loginViewModel.startedLogin.value == true) {
            Toast.makeText(this@LoginActivity, resources.getString(R.string.login_fail_msg), Toast.LENGTH_SHORT).show()
        }
    }

    private fun login() {
        val intent = Intent(
            Intent.ACTION_VIEW, Uri.parse(
                "${Constants.oauthLoginUrl}?client_id=${BuildConfig.CLIENT_ID}&scope=user+repo"
            )
        )
        loginViewModel.startedLogin.value = true
        startActivity(intent)
    }


}