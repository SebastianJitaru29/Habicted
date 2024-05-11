package com.example.habicted_app.data.repository.remote

import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.repository.GroupRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

class RemoteGroupRepository : GroupRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val groupCollection = if (auth.currentUser?.uid != null) {
        db.collection("users").document(auth.currentUser?.uid!!).collection("groups")
    } else {
        // Handle the case where the user is not signed in
        null
    }


    @Module
    @InstallIn(SingletonComponent::class)
    object RepositoryModule {

        @Singleton
        @Provides
        fun provideGroupRepository(): GroupRepository {
            return RemoteGroupRepository()
        }
    }

    override suspend fun getAllGroups(): List<Group> {
        val groupList = mutableListOf<Group>()

        // Retrieve all groups associated with the current user
        val querySnapshot = groupCollection?.get()?.await()
        if (querySnapshot != null) {
            for (document in querySnapshot.documents) {
                val group = document.toObject(Group::class.java)
                group?.let {
                    groupList.add(it)
                }
            }
        }

        return groupList
    }

    override suspend fun getGroup(groupId: Int): Group? {
        // Retrieve a specific group associated with the current user
        val documentSnapshot = groupCollection?.document(groupId.toString())?.get()?.await()
        return documentSnapshot?.toObject(Group::class.java)
    }

    override suspend fun getTasksForGroup(groupId: Int): List<Task> {
        // This method needs to be implemented based on how tasks are associated with groups in your Firestore database
        TODO("Not yet implemented")
    }

    override suspend fun insertGroup(group: Group): Long {
        // Convert the Group object into a map
        val groupMap = hashMapOf(
            "id" to group.id,
            "name" to group.name,
            "color" to group.color.toLong(),
            "members" to group.members,
            "tasksList" to group.tasksList
        )

        // Add the group document to Firestore associated with the current user
        groupCollection?.document(group.id.toString())?.set(groupMap)?.await()

        return group.id.toLong()
    }

    override suspend fun upsertGroup(group: Group) {
        // Update the group document in Firestore associated with the current user
        groupCollection?.document(group.id.toString())?.set(group)?.await()
    }

    override suspend fun deleteGroup(groupId: Int) {
        // Delete the group document from Firestore associated with the current user
        groupCollection?.document(groupId.toString())?.delete()?.await()
    }
}