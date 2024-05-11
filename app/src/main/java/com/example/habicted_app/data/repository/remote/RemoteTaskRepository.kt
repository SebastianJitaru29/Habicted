package com.example.habicted_app.data.repository.remote

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.repository.TaskRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteTaskRepository : TaskRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val taskCollection = if (auth.currentUser?.uid != null) {
        db.collection("users").document(auth.currentUser?.uid!!).collection("tasks")
    } else {
        // Handle the case where the user is not signed in
        null
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object RepositoryModule {
        @Singleton
        @Provides
        fun provideTaskRepository(): TaskRepository {
            return RemoteTaskRepository()
        }
    }

    override suspend fun getAllTasks(): List<Task> {
        val taskList = mutableListOf<Task>()

        // Retrieve all tasks associated with the current user
        val querySnapshot = taskCollection?.get()?.await()
        if (querySnapshot != null) {
            for (document in querySnapshot.documents) {
                val task = document.toObject(Task::class.java)
                task?.let {
                    taskList.add(it)
                }
            }
        }

        return taskList
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getTaskByDate(date: LocalDate): List<Task> {
        val taskList = mutableListOf<Task>()

        // Query tasks by date associated with the current user
        val querySnapshot = taskCollection?.whereEqualTo("date", date.toString())?.get()?.await()
        if (querySnapshot != null) {
            for (document in querySnapshot.documents) {
                //            val task = document.toObject(Task::class.java)
                val ddate = LocalDate.parse(document.get("date") as String)
                val task = Task(
                    id = document.get("id") as Long,
                    groupId = (document.get("groupId") as Long).toInt(),
                    name = (document.get("name") as String),
                    description = document.get("description") as String,
                    date = ddate,
                    isDone = document.get("isDone") as Boolean,
                    streakDays = (document.get("streakDays") as Long).toInt(),
                    doneBy = (document.get("doneBy") as Long).toInt(),
                    total = (document.get("total") as Long).toInt()
                )
                task.let {
                    taskList.add(it)
                }
            }
        }

        return taskList
    }

    override suspend fun getTaskByGroup(groupId: Int): List<Task> {
        val taskList = mutableListOf<Task>()

        // Query tasks by groupId associated with the current user
        val querySnapshot = taskCollection?.whereEqualTo("groupId", groupId)?.get()?.await()
        if (querySnapshot != null) {
            for (document in querySnapshot.documents) {
                val task = document.toObject(Task::class.java)
                task?.let {
                    taskList.add(it)
                }
            }
        }

        return taskList
    }

    override suspend fun insertTask(task: Task) {
        // Convert the Task object into a map
        val taskMap = hashMapOf(
            "id" to task.id,
            "groupId" to task.groupId,
            "name" to task.name,
            "description" to task.description,
            "date" to task.date.toString(), // Assuming date is stored as string
            "isDone" to task.isDone,
            "streakDays" to task.streakDays,
            "doneBy" to task.doneBy,
            "total" to task.total
        )

        // Add the task document to Firestore associated with the current user
        taskCollection?.document(task.id.toString())?.set(taskMap)?.await()
    }

    override suspend fun updateTask(newTask: Task): Boolean {
        TODO("Not yet implemented")
    }
}