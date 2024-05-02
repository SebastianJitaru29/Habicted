package com.example.habicted_app.data.repository.remote

import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.repository.TaskRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.LocalDate

@Module
@InstallIn(SingletonComponent::class)
class RemoteTaskRepository : TaskRepository {
    override fun getTasks(): List<Task> {
        TODO("Not yet implemented")
    }

    override fun getTaskByDate(date: LocalDate): List<Task> {
        TODO("Not yet implemented")
    }

    override fun getTaskByGroup(groupId: Int): List<Task> {
        TODO("Not yet implemented")
    }

    override fun insertTask(task: Task) {
        TODO("Not yet implemented")
    }
}