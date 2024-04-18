package com.example.habicted_app.screen.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val myPreferencesDataStore: MyPreferencesDataStore
): ViewModel(){
    val isCompleted = myPreferencesDataStore.taskStatusFlow.map {
        it.isLight
    }
    val priority = myPreferencesDataStore.taskStatusFlow.map {
        it.netpreference
    }

    fun updateIsCompleted(isCompleted:Boolean){
        viewModelScope.launch {
            myPreferencesDataStore.updateIsCompleted(isCompleted)
        }
    }

    fun updateNetworkPreference(priority: NetworkPreference){
        viewModelScope.launch {
            myPreferencesDataStore.updateNetworkPreference(priority)
        }
    }
}