package com.example.habicted_app.screen.taskscreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.habicted_app.HabictedApp
import com.example.habicted_app.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _userName: MutableStateFlow<String> = MutableStateFlow("")
    val userName: StateFlow<String> = _userName
    private fun setUserName(userName: String) {
        _userName.value = userName
    }

    val profilePicture: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    init {
        setUserName(firebaseAuth.currentUser?.email ?: "no name")
        Log.d("UserViewModel", "UserViewModel: ${firebaseAuth.currentUser?.displayName}," +
                " ${firebaseAuth.currentUser?.email}" + " ${firebaseAuth.currentUser?.photoUrl}")
        getUserParameter(firebaseAuth.currentUser?.uid ?: "") {
            if (it is String) {
                Log.d("PhotoUrl", "UserViewModel: $it")

            }
        }

        profilePicture.value = userRepository.getProfilePicture()
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as HabictedApp)
                val userRepository = application.container.userRepository
                UserViewModel(userRepository = userRepository)
            }
        }
    }
    private fun getUserParameter(userId: String, callback: (Any?) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()
        val userDocRef = firestore.collection("users").document(userId)

        userDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null && documentSnapshot.exists()) {
                val parameterValue =
                    documentSnapshot.getString("photoUrl") // Replace "parameterName" with your parameter name
                callback(parameterValue)
            } else {
                userDocRef.update("photoUrl", "")
                callback(null)
            }
        }.addOnFailureListener {
            callback(null)
        }
    }
}
