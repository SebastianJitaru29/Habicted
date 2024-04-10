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
        it.isCompleted
    }
    val priority = myPreferencesDataStore.taskStatusFlow.map {
        it.priority
    }

    fun updateIsCompleted(isCompleted:Boolean){
        viewModelScope.launch {
            myPreferencesDataStore.updateIsCompleted(isCompleted)
        }
    }

    fun updatePriority(priority: Priority){
        viewModelScope.launch {
            myPreferencesDataStore.updatePriority(priority)
        }
    }
}