package com.example.habicted_app.navigation.routes

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.habicted_app.data.model.Group
import com.example.habicted_app.screen.groups.GroupScreen
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupsRoute(groupList: List<Group>) {
    GroupScreen(
        groupList = groupList,
        searchUsersByEmail = ::searchUsersByEmail,
    )
}

//TODO: move to repository
fun searchUsersByEmail(email: String, callback: (List<String>) -> Unit): List<String> {
    val db = Firebase.firestore
    var users = listOf<String>()

    db.collection("users")
        .orderBy("email")
        .startAt(email)
        .endAt(email + "\uf8ff")
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                users = users + listOf(document.get("email") as String)
            }
            callback(users)
        }
        .addOnFailureListener { exception ->
            Log.w("", "Error getting documents: ", exception)
        }

    return users
}