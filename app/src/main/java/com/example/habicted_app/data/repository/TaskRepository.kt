package com.example.habicted_app.data.repository

import com.example.habicted_app.data.model.Task
import java.time.LocalDate

interface TaskRepository {
    suspend fun getAllTasks(): List<Task>
    suspend fun getTaskByDate(date: LocalDate): List<Task>
    suspend fun getTaskByGroup(groupId: Int): List<Task>
    suspend fun insertTask(task: Task)
    suspend fun updateTask(newTask: Task): Boolean
}