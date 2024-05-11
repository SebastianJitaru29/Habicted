package com.example.habicted_app.data.repository

import com.example.habicted_app.data.model.Task
import java.time.LocalDate

interface TaskRepository {
    fun getAllTasks(): List<Task>
    fun getTaskByDate(date: LocalDate): List<Task>
    fun getTaskByGroup(groupId: Int): List<Task>
    fun insertTask(task: Task)
    fun updateTask(newTask: Task): Boolean
}