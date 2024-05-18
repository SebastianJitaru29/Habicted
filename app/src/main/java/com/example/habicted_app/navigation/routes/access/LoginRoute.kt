package com.example.habicted_app.navigation.routes.access

import androidx.compose.runtime.Composable
import com.example.habicted_app.auth.model.AuthState
import com.example.habicted_app.screen.access.WelcomeScreen

@Composable
fun WelcomeRoute(
    onConfirmLogIn: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotedPassword: () -> Unit,
    state: AuthState,
    tryLogin: (String, String) -> Unit,
) {
    WelcomeScreen(
        onLogIn = {
            // If login ok then do:
            onConfirmLogIn()
        },
        onForgotedPassword = onNavigateToForgotedPassword,
        onSignUp = onNavigateToSignUp,
        state = state,
        tryLogin = tryLogin
    )
}