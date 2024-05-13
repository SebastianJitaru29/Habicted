package com.example.habicted_app.data.repository

import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Task

interface GroupRepository {
    suspend fun getAllGroups(): List<Group>
    suspend fun getGroup(groupId: Int): Group?
    suspend fun getTasksForGroup(groupId: Int): List<Task>
    suspend fun insertGroup(group: Group): Long
    suspend fun upsertGroup(group: Group)
    suspend fun deleteGroup(groupId: Int)

    suspend fun getGroupTasks(groupId: Int): List<Task>
    suspend fun addTaskToGroup(task: Task, groupId: Int)

    suspend fun getUserGroups(): List<Group>
}