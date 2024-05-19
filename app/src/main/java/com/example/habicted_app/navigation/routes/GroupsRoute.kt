package com.example.habicted_app.navigation.routes

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.User
import com.example.habicted_app.screen.groups.GroupScreen
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupsRoute(groupList: List<Group>, onInvite: (Group, String) -> Unit) {
    GroupScreen(
        groupList = groupList,
        searchUsersByEmail = ::searchUsersByEmail,
        onInvite = onInvite
    )
}

//TODO: move to repository
fun searchUsersByEmail(email: String, callback: (List<User>) -> Unit) {
    val db = Firebase.firestore
    var users = listOf<User>()

    db.collection("users")
        .orderBy("email")
        .startAt(email)
        .endAt(email + "\uf8ff")
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                try {
                    val user = User(
                        id = document.id,
                        email = document.get("email") as String,
                        name = ""
                    )
                    users = users + user
                } catch (e: Exception) {
                    Log.d("Error search users", e.toString())
                    throw e
                }
            }
            callback(users)
        }
        .addOnFailureListener { exception ->
            Log.w("", "Error getting documents: ", exception)
        }
}