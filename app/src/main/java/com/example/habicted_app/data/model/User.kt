package com.example.habicted_app.data.model

data class User (
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val groups: List<Group>,
)
