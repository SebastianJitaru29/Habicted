package com.example.habicted_app.screen.groups

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.habicted_app.HabictedApp
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.repository.GroupRepository
import com.example.habicted_app.data.repository.local.LocalGroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class GroupsViewModel @Inject constructor(private val groupRepository: GroupRepository) : ViewModel() {

    private val _groupList = MutableStateFlow<List<GroupUIState>>(emptyList())
    val groupList: MutableStateFlow<List<GroupUIState>> = _groupList


    init {
        _groupList.value = getAllGroups()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAllGroups(): List<GroupUIState> {
        return groupRepository.getAllGroups().map { group ->
            GroupUIState(
                name = group.name,
                members = group.members,
                tasks = group.tasksList,
                isInStreak = isInStreak(group.tasksList),
                streakDays = getStreakDays(group.tasksList)
            )
        }
    }

    fun addGroup() {
        val newGroup = GroupUIState(
            name = "New Group",
            members = emptyList(),
            tasks = emptyList(),
            isInStreak = false,
            streakDays = 2
        )
        val currentList = _groupList.value ?: emptyList()
        val updatedList = currentList.toMutableList()
        updatedList.add(newGroup)
        _groupList.value = updatedList
    }

    private fun isInStreak(tasks: List<Task>): Boolean {
        return tasks.all { it.isDone }
    }

    private fun getStreakDays(tasks: List<Task>): Int {
        return if (tasks.any { it.streakDays == 0 }) {
            0
        } else {
            tasks.sumOf { it.streakDays }
        }
    }

//    companion object {
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val application = (this[APPLICATION_KEY] as HabictedApp)
//                val groupRepository = application.container.groupRepository
//                GroupsViewModel(groupRepository = groupRepository)
//            }
//        }
//    }


}