package com.example.gitreposearch.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitreposearch.data.UserInfo

class ProfileViewModel: ViewModel() {

    val userInfo = MutableLiveData<UserInfo>()

}