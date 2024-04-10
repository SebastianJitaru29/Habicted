package com.example.habicted_app.screen.taskscreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.habicted_app.HabictedApp
import com.example.habicted_app.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class TasksViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    private val _displayTasks = MutableStateFlow<List<TaskUIState>>(emptyList())
    val displayTasks: StateFlow<List<TaskUIState>> = _displayTasks

    init {
        loadTasks()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadTasks() {
        val tasks = taskRepository.getTaskByDate(LocalDate.now())
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


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as HabictedApp)
                val taskRepository = application.container.taskRepository
                TasksViewModel(taskRepository = taskRepository)
            }
        }
    }
}