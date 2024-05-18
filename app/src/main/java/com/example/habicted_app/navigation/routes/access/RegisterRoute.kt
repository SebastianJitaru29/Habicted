package com.example.habicted_app.navigation.routes.access

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habicted_app.auth.model.signup.SignUpViewModel
import com.example.habicted_app.screen.access.RegisterScreen

@Composable
fun RegisterRoute(onBack: () -> Unit) {
    val viewModel: SignUpViewModel = hiltViewModel()
    RegisterScreen(
        onBack = onBack,
        signUpState = viewModel.signUpState.collectAsState(initial = null),
        onRegister = { email, password ->
            viewModel.registerUser(email, password)
        })
}