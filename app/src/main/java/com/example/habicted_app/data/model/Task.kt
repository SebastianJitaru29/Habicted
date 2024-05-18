package com.example.habicted_app.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.DocumentSnapshot
import java.time.LocalDate

data class Task(
    val id: Long,
    val groupId: String,
    val name: String,
    val description: String?,
    val date: LocalDate = LocalDate.now(),
    val isDone: Boolean,
    val streakDays: Int,
    val doneBy: Int,
    val total: Int,
) {
    @RequiresApi(Build.VERSION_CODES.O)
    constructor() : this(
        0,
        "",
        "",
        "",
        LocalDate.now(),
        false,
        0,
        0,
        0
    )

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(document: DocumentSnapshot) : this(
        id = document.get("id") as Long,
        groupId = document.get("groupId") as String,
        name = document.get("name") as String,
        description = document.get("description") as String,
        date = LocalDate.parse(document.get("date") as String),
        isDone = document.get("isDone") as Boolean,
        streakDays = (document.get("streakDays") as Long).toInt(),
        doneBy = (document.get("doneBy") as Long).toInt(),
        total = (document.get("total") as Long).toInt()
    )

}
