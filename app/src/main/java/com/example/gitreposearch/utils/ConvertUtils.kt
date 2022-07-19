package com.example.gitreposearch.utils

import com.example.gitreposearch.data.RepoError
import com.example.gitreposearch.network.GithubApiImpl
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Converter
import java.text.DecimalFormat
import java.util.*
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

object ConvertUtils {

    private val random = Random()

    fun getRandomColorCode(): String{
        val randomInt = random.nextInt(0xffffff+1)
        return String.format("#%06x", randomInt)
    }

    fun getErrorResponseMsg(errorBody: ResponseBody): String{
        return Gson().fromJson(errorBody.charStream(), RepoError::class.java).message
    }

    fun getStarredString(starredCount: Int): String{
        val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T')
        val value = floor(log10(starredCount.toDouble())).toInt()
        val base = value / 3
        return if (value >= 3 && base < suffix.size) {
            "${String.format("%.1f",starredCount/10.0.pow(base*3))}${suffix[base]}"
        } else {
            starredCount.toString()
        }
    }
}