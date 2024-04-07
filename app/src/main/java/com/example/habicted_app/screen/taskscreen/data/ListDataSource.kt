package com.example.habicted_app.screen.taskscreen.data

import com.example.habicted_app.R

class ListDataSource {
    companion object {
        val taskList: MutableList<TaskListElement> = mutableListOf(
            TaskListElement(R.string.tasca1, R.drawable.outline_groups_24, false),
            TaskListElement(R.string.tasca2, R.drawable.outline_groups_24, false),
            TaskListElement(R.string.tasca3, R.drawable.outline_groups_24, false),
            TaskListElement(R.string.tasca1, R.drawable.outline_groups_24, false),
            TaskListElement(R.string.tasca2, R.drawable.outline_groups_24, false),
            TaskListElement(R.string.tasca3, R.drawable.outline_groups_24, false),
            TaskListElement(R.string.tasca1, R.drawable.outline_groups_24, false),
            TaskListElement(R.string.tasca2, R.drawable.outline_groups_24, false),
            TaskListElement(R.string.tasca3, R.drawable.outline_groups_24, false)
        )
    }

    fun initialLoadList(): List<TaskListElement> {
        return taskList
    }

    fun addTask(task: TaskListElement) {
        taskList.add(task)
    }
}
