package com.example.habicted_app.screen.home

import com.example.habicted_app.data.model.Group
import com.example.habicted_app.screen.taskscreen.TaskUIState

data class HomeUIState(
    val tasks: List<TaskUIState> = emptyList(),
    val groups: List<Group> = emptyList(),
)