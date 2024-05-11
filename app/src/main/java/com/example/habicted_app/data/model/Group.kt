package com.example.habicted_app.data.model

data class Group(
    val id: Int,
    val name: String,
    val color: ULong = 0xFF000000u,
    val members: List<User>,
    val tasksList: List<Task>,
)