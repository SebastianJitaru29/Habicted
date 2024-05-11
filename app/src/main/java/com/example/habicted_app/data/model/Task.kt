package com.example.habicted_app.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

data class Task(
    val id: Int,
    val groupId: Int,
    val name: String,
    val description: String?,
    val date: LocalDate,
    val isDone: Boolean,
    val streakDays: Int,
    val doneBy: Int,
    val total: Int,
) {
    @RequiresApi(Build.VERSION_CODES.O)
    constructor() : this(
        0,
        0,
        "",
        "",
        LocalDate.now(),
        false,
        0,
        0,
        0
    )
}
