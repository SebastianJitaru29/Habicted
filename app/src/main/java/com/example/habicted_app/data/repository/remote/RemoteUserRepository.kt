package com.example.habicted_app.data.repository.remote

import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RemoteUserRepository : UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val userCollection = if (auth.currentUser?.uid != null) {
        db.collection("users").document(auth.currentUser?.uid!!)
    } else {
        // Handle the case where the user is not signed in
        null
    }
    override fun getUserName(): String {
        TODO("Not yet implemented")
    }

    override fun setUserName(userName: String) {
        TODO("Not yet implemented")
    }

    override fun getProfilePicture(): Int {
        TODO("Not yet implemented")
    }

    override fun setProfilePicture(profilePicture: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserGroups(): List<Group> {
        TODO()
    }
}