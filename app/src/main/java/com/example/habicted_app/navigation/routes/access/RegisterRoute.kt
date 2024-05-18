package com.example.habicted_app.navigation.routes.access

import androidx.compose.runtime.Composable
import com.example.habicted_app.auth.model.AuthState
import com.example.habicted_app.screen.access.RegisterScreen

@Composable
fun RegisterRoute(
    onBack: () -> Unit,
    signUpState: AuthState,
    onRegister: (String, String) -> Unit,
) {
    RegisterScreen(
        onBack = onBack,
        signUpState = signUpState,
        onRegister = onRegister
    )
}