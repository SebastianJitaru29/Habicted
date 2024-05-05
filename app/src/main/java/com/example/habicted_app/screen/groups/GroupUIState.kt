package com.example.habicted_app.screen.groups

import androidx.compose.ui.graphics.Color
import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.model.User

data class GroupUIState(
    val name: String,
    val members: List<User>,
    val color: Color = Color.Transparent,
    val tasks: List<Task>,
    val isInStreak: Boolean,
    val streakDays: Int,
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
