package com.example.habicted_app.screen.profileScreen

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _profilePicUri = MutableStateFlow(Uri.EMPTY)
    val profilePicUri: Uri = _profilePicUri.asStateFlow().value


    fun updateProfilePic(newUri: Uri) {
        _profilePicUri.value = newUri
    }

}