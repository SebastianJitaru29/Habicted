package com.example.habicted_app.data.repository.local

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.model.User
import com.example.habicted_app.data.repository.GroupRepository
import com.example.habicted_app.ui.theme.Amber500
import com.example.habicted_app.ui.theme.Red500
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.LocalDate
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalGroupRepository : GroupRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    private val allGroups: MutableList<Group> = mutableListOf(
        Group(
            id = 1,
            name = "Group 1",
            members = listOf(
                User(1, "", "", "", emptyList()),
            ),
            color = Red500.value,
            tasksList = listOf(
                Task(
                    id = 1,
                    groupId = 1,
                    name = "Task 1",
                    description = null,
                    date = LocalDate.now(),
                    isDone = false,
                    streakDays = 1,
                    done = 1,
                    total = 2,
                ),
                Task(
                    id = 2,
                    groupId = 2,
                    name = "Task 2",
                    description = null,
                    date = LocalDate.now(),
                    isDone = false,
                    streakDays = 1,
                    done = 1,
                    total = 2,
                ),
            )
        ),
        Group(
            id = 2,
            name = "Group 2",
            members = listOf(
                User(1, "User1", "", "", emptyList()),
            ),
            color = Amber500.value,
            tasksList = emptyList(),
        )
    )

    @Module
    @InstallIn(SingletonComponent::class)
    object RepositoryModule {

        @Singleton
        @Provides
        fun provideGroupRepository(): GroupRepository {
            return LocalGroupRepository()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAllGroups(): List<Group> {
        return allGroups
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getTasksForGroup(groupId: Int): List<Task> {
        return getGroup(groupId)?.tasksList ?: emptyList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getGroup(groupId: Int): Group? {
        return getAllGroups().firstOrNull { it.id == groupId }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun insertGroup(group: Group): Long {
        allGroups.add(group)
        return group.id.toLong()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun upsertGroup(group: Group) {
        allGroups.removeIf { it.id == group.id }
        allGroups.add(group)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deleteGroup(groupId: Int) {
        allGroups.removeIf { it.id == groupId }
    }
}