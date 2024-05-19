package com.example.habicted_app.data.repository.remote

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.repository.GroupRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.messaging
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
        db.collection("Groups")
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
                val group = Group(document)
                groupList.add(group)
            }
        }

        return groupList
    }

    override suspend fun getGroup(groupId: String): Group? {
        // Retrieve a specific group associated with the current user
        val documentSnapshot = groupCollection?.document(groupId.toString())?.get()?.await()
        var group: Group? = null
        documentSnapshot?.let {
            group = Group(it)
        }
        return group
    }


    override suspend fun insertGroup(group: Group): String {
        // Convert the Group object into a map
        val groupMap = hashMapOf(
            "id" to group.id,
            "name" to group.name,
            "color" to group.color.toLong(),
            "members" to group.members,
            "tasksList" to group.tasksList
        )

        // Add the group document to Firestore associated with the current user
        val documentId = groupCollection?.add(groupMap)?.await()
        if (documentId != null) {
            group.id = documentId.id
            groupMap["id"] = documentId.id
            documentId.set(groupMap).await()
            db.collection("users").document(auth.currentUser?.uid!!)
                .update("groupsIDs", FieldValue.arrayUnion(documentId.id))
        }

        return group.id
    }

    override suspend fun upsertGroup(group: Group) {
        // Update the group document in Firestore associated with the current user
        groupCollection?.document(group.id)?.set(group)?.await()
    }

    override suspend fun deleteGroup(groupId: String) {
        // Delete the group document from Firestore associated with the current user
        groupCollection?.document(groupId.toString())?.delete()?.await()
    }

    override suspend fun addTaskToGroup(task: Task, groupId: String) {
        // Reference to the group's taskList subcollection
        val groupTaskListRef = groupCollection
            ?.document(groupId)
            ?.collection("taskList")

        // Convert the Task object into a map
        val taskMap = hashMapOf(
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
        try {
            val documentRef = groupTaskListRef?.add(taskMap)?.await()
            val documentId = documentRef?.id
            if (documentId != null) {
                taskMap["id"] = documentId
                documentRef.set(taskMap).await()
                task.id = documentId
            }
        } catch (e: Exception) {
            Log.d("Remote Group", "Error while adding task: $e")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getGroupTasks(groupId: String): List<Task> {
        val taskList = mutableListOf<Task>()

        // Reference to the group's taskList subcollection
        val groupTaskListRef = groupCollection
            ?.document(groupId)
            ?.collection("taskList")

        // Retrieve all tasks in the group's taskList subcollection
        val querySnapshot = groupTaskListRef?.get()?.await()

        // Convert documents to Task objects
        if (querySnapshot != null) {
            for (document in querySnapshot.documents) {
                try {
                    val task = Task(document)
                    taskList.add(task)
                } catch (e: Exception) {
                    Log.d("RemoteGroupRepository", "getGroupTasks: Error while getting tasks: ${e}")
                }
            }
        }

        Log.d("RemoteGroupRepository", "getGroupTasks: $taskList")
        return taskList.toList()
    }

    override suspend fun getUserGroups(): List<Group> {
        val userId = auth.currentUser?.uid ?: return emptyList()

        // Reference to the user document
        val userDocumentRef = db.collection("users").document(userId)
        val userDocumentSnapshot = userDocumentRef.get().await()

        // If the user document doesn't exist, create one with default values
        if (!userDocumentSnapshot.exists()) {
            val defaultData = hashMapOf(
                "groupsIDs" to listOf<String>() // You can add other default fields here
            )
            userDocumentRef.set(defaultData).await()
            return emptyList() // Return empty list since the user just got created
        }

        // Retrieve the groupsIDs array from the user document
        val groupsIDs = userDocumentSnapshot.get("groupsIDs") as? List<*> ?: return emptyList()

        val groupList = mutableListOf<Group>()

        // For each groupId in the groupsIDs array, retrieve the corresponding group document
        for (groupId in groupsIDs) {
            val groupIdStr = groupId.toString()
            if (groupIdStr.isNotEmpty()) {
                val documentSnapshot = db.collection("Groups").document(groupIdStr).get().await()
                if (documentSnapshot.exists()) {
                    val group = Group(documentSnapshot)
                    groupList.add(group)
                }
            }
        }

        Log.d("RemoteGroupRepository", "getUserGroups: $groupList")
        return groupList
    }

    fun subscribeToMessagingTopic(topic: String) {
        Firebase.messaging.subscribeToTopic(topic).addOnCompleteListener { task ->
            var msg = "Subscribed"
            if (!task.isSuccessful) {
                msg = "Subscribed Failed"
            }
            Log.d("TAG", msg)
        }
    }


    fun updateTasksStatus(
        task: Task,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {},
    ) {
        val taskRef = task.id?.let {
            groupCollection
                ?.document(task.groupId)
                ?.collection("taskList")
                ?.document(it)
        }

        taskRef?.update("isDone", task.isDone)?.addOnSuccessListener {
            onSuccess()
        }?.addOnFailureListener {
            onFailure(it)
        }
    }
}