package com.example.habicted_app.screen.taskscreen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habicted_app.data.repository.TaskRepository
import java.time.LocalDate

class TasksViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    private val _displayTasks = MutableLiveData<List<TaskUIState>>()
    val displayTasks: LiveData<List<TaskUIState>> = _displayTasks

    init {
        loadTasks()
    }

    fun loadTasks() {
        val tasks = taskRepository.getTasks()
        _displayTasks.value = tasks.map {
            TaskUIState(
                name = it.name,
                color = "",
                date = it.date,
                isChecked = it.isDone
            )
        }
    }

    fun displayTasksByDate(date: LocalDate) {
        val tasks = taskRepository.getTaskByDate(date)
        _displayTasks.value = tasks.map {
            TaskUIState(
                name = it.name,
                color = "",
                date = it.date,
                isChecked = it.isDone
            )
        }
    }

    fun displayTasksByGroup(groupId: Int) {
        val tasks = taskRepository.getTaskByGroup(groupId)
        _displayTasks.value = tasks.map {
            TaskUIState(
                name = it.name,
                color = "",
                date = it.date,
                isChecked = it.isDone
            )
        }
    }
}