package com.example.habicted_app.data.repository.local

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.repository.TaskRepository
import java.time.LocalDate

class LocalTaskRepository : TaskRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    private val tasks = listOf<Task>(
        Task(1, 1, "Task 1", "Description 1", LocalDate.now(), false, 0, 0, 0),
        Task(2, 1, "Task 1", "Description 1", LocalDate.now(), true, 0, 0, 0),
        Task(3, 2, "Task 2", "Description 2", LocalDate.now().plusDays(2), false, 0, 0, 0),
        Task(4, 2, "Task 2", "Description 2", LocalDate.now().plusDays(2), false, 0, 0, 0),
        Task(5, 1, "Task 3", "Description 3", LocalDate.now().plusDays(3), false, 0, 0, 0),
        Task(6, 1, "Task 4", "Description 4", LocalDate.now().plusDays(4), false, 0, 0, 0),
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getTasks(): List<Task> {
        return tasks
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getTaskByDate(date: LocalDate): List<Task> {
        return tasks.filter { it.date == date }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getTaskByGroup(groupId: Int): List<Task> {
        return tasks.filter { it.groupId == groupId }
    }
}