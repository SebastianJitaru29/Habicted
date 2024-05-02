package com.example.habicted_app.data.repository.remote

import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.repository.GroupRepository


class RemoteGroupRepository : GroupRepository {
    override fun getAllGroups(): List<Group> {
        TODO("Not yet implemented")
    }

    override fun getGroup(groupId: Int): Group? {
        TODO("Not yet implemented")
    }

    override fun getTasksForGroup(groupId: Int): List<Task> {
        TODO("Not yet implemented")
    }

    override fun insertGroup(group: Group): Long {
        TODO("Not yet implemented")
    }

    override fun upsertGroup(group: Group) {
        TODO("Not yet implemented")
    }

    override fun deleteGroup(groupId: Int) {
        TODO("Not yet implemented")
    }
}