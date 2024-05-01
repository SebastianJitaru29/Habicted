package com.example.habicted_app.auth.model.forgotpas

data class ForgotPassState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)