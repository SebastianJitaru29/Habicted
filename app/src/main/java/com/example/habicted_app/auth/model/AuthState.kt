package com.example.habicted_app.auth.model

data class AuthState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = "",
)
