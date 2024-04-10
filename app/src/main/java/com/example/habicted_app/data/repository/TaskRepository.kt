package com.example.habicted_app.data.repository

import com.example.habicted_app.data.model.Task
import java.time.LocalDate

interface TaskRepository {
    fun getTasks(): List<Task>
    fun getTaskByDate(date: LocalDate): List<Task>
    fun getTaskByGroup(groupId: Int): List<Task>
    fun addTask(task: Task)
}