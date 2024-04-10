package com.example.habicted_app.screen

import androidx.lifecycle.ViewModel
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val tasksRepository: TaskRepository) : ViewModel() {
    fun addTask(newTask: Task) {
        tasksRepository.addTask(newTask)
    }
}