package com.example.habicted_app.data.repository

interface UserRepository {
    fun getUserName(): String
    fun setUserName(userName: String)
    fun getProfilePicture(): Int
    fun setProfilePicture(profilePicture: Int)
}