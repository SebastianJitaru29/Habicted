package com.example.habicted_app.data.model

data class Group(
    val id: Int,
    val name: String,
    val color: String = "",
    val members: List<User>,
    val tasksList: List<Task>,
)