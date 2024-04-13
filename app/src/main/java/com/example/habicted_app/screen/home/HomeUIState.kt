package com.example.habicted_app.screen.home

import com.example.habicted_app.screen.groups.GroupUIState
import com.example.habicted_app.screen.taskscreen.TaskUIState

data class HomeUIState(
    val tasks: List<TaskUIState> = emptyList(),
    val groups: List<GroupUIState> = emptyList(),
)