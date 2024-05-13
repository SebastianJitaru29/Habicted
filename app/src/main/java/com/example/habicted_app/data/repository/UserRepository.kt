package com.example.habicted_app.data.repository

import com.example.habicted_app.data.model.Group

interface UserRepository {
    fun getUserName(): String
    fun setUserName(userName: String)
    fun getProfilePicture(): Int
    fun setProfilePicture(profilePicture: Int)

    suspend fun getUserGroups(): List<Group>
}