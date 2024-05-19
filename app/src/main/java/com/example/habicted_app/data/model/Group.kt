package com.example.habicted_app.data.model

import com.google.firebase.firestore.DocumentSnapshot

data class Group(
    var id: String,
    val name: String,
    val color: ULong = 0xFF000000u,
    var members: List<String>,
    val tasksList: List<Task>,
) {
    constructor() : this("", "", 0xFF000000u, emptyList(), emptyList())
    constructor(document: DocumentSnapshot) : this(
        document.id,
        document.get("name") as String,
        (document.get("color") as Long).toULong(),
        document.get("members") as List<String>,
        document.get("tasksList") as List<Task>
    )
}