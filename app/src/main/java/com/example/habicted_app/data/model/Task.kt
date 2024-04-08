package com.example.habicted_app.data.model

import java.time.LocalDate

data class Task(
    val id: Int,
    val name: String,
    val description: String?,
    val date: LocalDate,
    val isDone: Boolean,
    val streakDays: Int,
    val done: Int,
    val total: Int
)
