package com.example.habicted_app.screen.taskscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habicted_app.data.repository.UserRepository

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _userName: MutableLiveData<String> = MutableLiveData<String>()
    val userName: LiveData<String> = _userName
    private fun setUserName(userName: String) {
        _userName.value = userName
    }

    val profilePicture: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    init {
        setUserName(userRepository.getUserName())
        profilePicture.value = userRepository.getProfilePicture()
    }


}