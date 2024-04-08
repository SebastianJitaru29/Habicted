package com.example.habicted_app.screen.taskscreen

import java.time.LocalDate

data class TaskUIState (
    val name: String,
    val color: String,
    val date: LocalDate,
    val isChecked: Boolean
)