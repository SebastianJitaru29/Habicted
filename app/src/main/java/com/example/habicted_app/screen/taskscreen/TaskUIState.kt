package com.example.habicted_app.screen.taskscreen

import androidx.compose.ui.graphics.Color
import com.example.habicted_app.data.model.Task

data class TaskUIState(
    val task: Task,
    val color: Color = Color.Transparent,
    val groupName: String = "",
)
