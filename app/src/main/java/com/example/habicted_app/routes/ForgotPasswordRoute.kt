package com.example.habicted_app.routes

import androidx.compose.runtime.Composable
import com.example.habicted_app.screen.access.ForgotPasswordScreen
import com.example.habicted_app.screen.access.RegisterScreen

@Composable
fun ForgotPasswordRoute(onBack: () -> Unit) {

    ForgotPasswordScreen(onBack = onBack )
}