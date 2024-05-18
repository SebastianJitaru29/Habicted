package com.example.habicted_app.data.repository

import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Task

interface GroupRepository {
    suspend fun getAllGroups(): List<Group>
    suspend fun getGroup(groupId: String): Group?
    suspend fun insertGroup(group: Group): String
    suspend fun upsertGroup(group: Group)
    suspend fun deleteGroup(groupId: String)
    suspend fun getGroupTasks(groupId: String): List<Task>
    suspend fun addTaskToGroup(task: Task, groupId: String)

    suspend fun getUserGroups(): List<Group>
}