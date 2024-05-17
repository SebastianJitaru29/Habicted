package com.example.habicted_app.screen.preferences

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.functions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val myPreferencesDataStore: MyPreferencesDataStore
): ViewModel() {
    private val functions: FirebaseFunctions = FirebaseFunctions.getInstance()
    val isCompleted = myPreferencesDataStore.taskStatusFlow.map {
        it.isLight
    }
    val priority = myPreferencesDataStore.taskStatusFlow.map {
        it.netpreference
    }

    fun updateIsCompleted(isCompleted: Boolean) {
        viewModelScope.launch {
            myPreferencesDataStore.updateIsCompleted(isCompleted)
        }
    }

    fun updateNetworkPreference(priority: NetworkPreference) {
        viewModelScope.launch {
            myPreferencesDataStore.updateNetworkPreference(priority)
        }
    }
    //Launch function for testing cloud functions
    fun launchFunction() {
        viewModelScope.launch {
            Log.d("TAG", "launchFunction")

            functions.getHttpsCallable("helloWorld2").call()
                .addOnSuccessListener { result ->
                    try {
                        val data = result.data as? Map<*, *>
                        if (data != null) {
                            val message = data["message"] as? String
                            Log.d("TAG", "launchFunction: $message")
                        } else {
                            Log.d("TAG", "launchFunction: Response is missing data field.")
                        }
                    } catch (e: Exception) {
                        Log.e("TAG", "launchFunction: Failed to parse response", e)
                    }
                }
                .addOnFailureListener { e ->
                    Log.d("TAG", "launchFunction: ${e.message}")
                }
        }
    }
    //Get token of device for cloud messaging testing
    fun getCurrentRegistrationToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("TAG1","Getching token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result

            Log.d("TAG", "getCurrentRegistrationToken: $token")
        }
    }

    fun subscribeToMessagingTopic(topic:String){
        Firebase.messaging.subscribeToTopic(topic).
        addOnCompleteListener { task ->
            var msg = "Subscribed"
            if (!task.isSuccessful) {
                msg = "Subscribed Failed"
            }
            Log.d("TAG", msg)
        }
    }
}