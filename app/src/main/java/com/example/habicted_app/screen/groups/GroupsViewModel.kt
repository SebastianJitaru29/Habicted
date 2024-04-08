package com.example.habicted_app.screen.groups

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.repository.GroupRepository

class GroupsViewModel(private val repository: GroupRepository) : ViewModel() {

    val groups: MutableLiveData<List<GroupUIState>> = MutableLiveData()

    init {
        groups.value = getAllGroups()
    }

    private fun getAllGroups(): List<GroupUIState> {
        return repository.getAllGroups().map { group ->
            GroupUIState(
                name = group.name,
                members = group.members,
                tasks = group.tasksList,
                isInStreak = isInStreak(group.tasksList),
                streakDays = getStreakDays(group.tasksList)
            )
        }

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


}