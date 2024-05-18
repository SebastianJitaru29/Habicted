package com.example.habicted_app.data.model

import com.google.firebase.firestore.DocumentSnapshot

data class Group(
    val id: Int,
    val name: String,
    val color: ULong = 0xFF000000u,
    val members: List<User>,
    val tasksList: List<Task>,
) {
    constructor() : this(0, "", 0xFF000000u, emptyList(), emptyList())
    constructor(document: DocumentSnapshot) : this(
        (document.get("id") as Long).toInt(),
        document.get("name") as String,
        (document.get("color") as Long).toULong(),
        document.get("members") as List<User>,
        document.get("tasksList") as List<Task>
    )
}