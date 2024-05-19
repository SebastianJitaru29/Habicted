package com.example.habicted_app.screen.groups

import androidx.compose.ui.graphics.Color
import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Task

data class GroupUIState(
    val name: String = "",
    val members: List<String> = emptyList(),
    val color: Color = Color.Transparent,
    val tasks: List<Task> = emptyList(),
    val isInStreak: Boolean = false,
    val streakDays: Int = 0,
) {
    constructor(group: Group) : this(
        name = group.name,
        members = group.members,
        color = Color(group.color),
        tasks = group.tasksList,
        isInStreak = false,
        streakDays = 0,
    )
}
