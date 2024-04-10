package com.example.habicted_app.data.repository

import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Task

interface GroupRepository {
    fun getAllGroups(): List<Group>
    fun getGroup(groupId: Int): Group?
    fun getTasksForGroup(groupId: Int): List<Task>
    fun insertGroup(group: Group): Long
    fun upsertGroup(group: Group)
    fun deleteGroup(groupId: Int)
}