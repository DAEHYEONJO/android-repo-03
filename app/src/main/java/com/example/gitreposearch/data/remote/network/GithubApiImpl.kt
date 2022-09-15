package com.example.gitreposearch.data.remote.network

import com.example.gitreposearch.BuildConfig
import com.example.gitreposearch.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object GithubApiImpl {

    private val headerInterceptor = Interceptor{ chain ->
        with(chain){
            val request = request().newBuilder()
                .addHeader("Accept","application/json")
                .build()
            proceed(request)
        }
    }

    private val loggerInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG){
            HttpLoggingInterceptor.Level.BODY
        }else{
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(loggerInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.githubBaseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    val githubApi: GithubApi = retrofit.create(GithubApi::class.java)

}