package com.example.habicted_app.screen.groups

import androidx.compose.ui.graphics.Color
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.model.User
import com.example.habicted_app.screen.taskscreen.data.CalendarData

data class GroupUIState(
    val name: String,
    val members: List<User>,
    val tasks: List<Task>,
    val isInStreak: Boolean,
    val streakDays: Int,
)

data class TaskInfo(
    val name: String,
    val color: Color,
    val date: CalendarData.Date,
    val done: Int,
    val total: Int
)

data class UserInfo(val name: String, val email: String?, val image: String?)
