package com.example.habicted_app.screen.taskscreen

import com.example.habicted_app.data.model.Task
import java.time.LocalDate

data class TaskUIState(
    val name: String,
    val color: String? = null,
    val date: LocalDate,
    val isChecked: Boolean,
) {
    constructor(task: Task) : this(
        name = task.name,
        color = null,
        date = task.date,
        isChecked = task.isDone
    )
}