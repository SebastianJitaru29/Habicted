package com.example.habicted_app.data.repository.remote

import android.util.Log
import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.repository.GroupRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Singleton

class RemoteGroupRepository : GroupRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val groupCollection = if (auth.currentUser?.uid != null) {
        db.collection("groupss")
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

        db.collection("users").document(auth.currentUser?.uid!!)
            .update("groupsIDs", FieldValue.arrayUnion(group.id))
            .await()

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

    override suspend fun addTaskToGroup(task: Task, groupId: Int) {
        // Reference to the group's taskList subcollection
        val groupTaskListRef = db.collection("users")
            .document(auth.currentUser?.uid!!)
            .collection("groups")
            .document(groupId.toString())
            .collection("taskList")

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

        // Add the task document to the group's taskList subcollection
        groupTaskListRef.add(taskMap).await()
    }

    override suspend fun getGroupTasks(groupId: Int): List<Task> {
        val taskList = mutableListOf<Task>()

        // Reference to the group's taskList subcollection
        val groupTaskListRef = db.collection("users")
            .document(auth.currentUser?.uid!!)
            .collection("groups")
            .document(groupId.toString())
            .collection("taskList")

        // Retrieve all tasks in the group's taskList subcollection
        val querySnapshot = groupTaskListRef.get().await()

        // Convert documents to Task objects
        for (document in querySnapshot.documents) {
            val task = Task(
                id = document.get("id") as Long,
                groupId = (document.get("groupId") as Long).toInt(),
                name = document.get("name") as String,
                description = document.get("description") as String,
                date = LocalDate.parse(document.get("date") as String),
                isDone = document.get("isDone") as Boolean,
                streakDays = (document.get("streakDays") as Long).toInt(),
                doneBy = (document.get("doneBy") as Long).toInt(),
                total = (document.get("total") as Long).toInt()
            )
            taskList.add(task)
        }

        return taskList.toList()
    }

    override suspend fun getUserGroups(): List<Group> {
        val groupList = mutableListOf<Group>()

        // Retrieve the groupsIDs array from the user document
        val groupsIDs = db.collection("users").document(auth.currentUser?.uid!!)
            .get().await().get("groupsIDs") as List<*>

        // For each groupId in the groupsIDs array, retrieve the corresponding group document
        for (groupId in groupsIDs) {
            val documentSnapshot =
                db.collection("groupss").document(groupId.toString()).get().await()
            val group = documentSnapshot.toObject(Group::class.java)
            if (group != null) {
                groupList.add(group)
            }
        }

        Log.d("RemoteGroupRepository", "getUserGroups: $groupList")
        return groupList
    }
}