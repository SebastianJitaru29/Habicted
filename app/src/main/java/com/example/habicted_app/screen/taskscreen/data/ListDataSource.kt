package com.example.habicted_app.screen.taskscreen.data

import com.example.habicted_app.R
import com.example.habicted_app.screen.taskscreen.models.TaskListElement

class ListDataSource(){
    fun loadTasks(): List<TaskListElement> {
        return listOf<TaskListElement>(
            TaskListElement(R.string.tasca1,R.drawable.outline_groups_24),
            TaskListElement(R.string.tasca2,R.drawable.outline_groups_24),
            TaskListElement(R.string.tasca3,R.drawable.outline_groups_24)
        )
    }
}