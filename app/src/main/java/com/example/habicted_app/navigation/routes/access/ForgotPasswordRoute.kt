package com.example.habicted_app.navigation.routes.access

import androidx.compose.runtime.Composable
import com.example.habicted_app.auth.model.AuthState
import com.example.habicted_app.screen.access.ForgotPasswordScreen

@Composable
fun ForgotPasswordRoute(onBack: () -> Unit, recoverPassword: (String) -> Unit, state: AuthState) {
    ForgotPasswordScreen(
        onBack = onBack,
        recoverPassword = recoverPassword,
        state = state
    )
}