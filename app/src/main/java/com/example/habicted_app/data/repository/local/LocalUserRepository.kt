package com.example.habicted_app.data.repository.local

import com.example.habicted_app.R
import com.example.habicted_app.data.repository.UserRepository

class LocalUserRepository : UserRepository {
    private var userName: String = "User"
    private var profilePicture: Int = R.drawable.outline_groups_24

    override fun getUserName(): String {
        return userName
    }

    override fun setUserName(userName: String) {
        this.userName = userName
    }

    override fun getProfilePicture(): Int {
        return profilePicture
    }

    override fun setProfilePicture(profilePicture: Int) {
        this.profilePicture = profilePicture
    }
}