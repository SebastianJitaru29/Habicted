package com.example.habicted_app.screen.taskscreen

import com.example.habicted_app.data.model.Task


sealed class TaskUIEvents {
    data class ConvertTaskToTaskUIState(val task: Task) : TaskUIEvents()
    data class UpdateIsDone(val isDone: Boolean, val task: Task) : TaskUIEvents()
}