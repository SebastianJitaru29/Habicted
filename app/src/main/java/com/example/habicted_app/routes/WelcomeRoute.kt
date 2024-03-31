package com.example.habicted_app.routes

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habicted_app.screen.WelcomeScreen
import com.example.habicted_app.viewModels.WelcomeViewModel

@Composable
fun WelcomeRoute(
    onNavigateToSignIn: (email: String) -> Unit,
    onNavigateToSignUp: (email: String) -> Unit,
) {
    val welcomeViewModel: WelcomeViewModel = viewModel()

    WelcomeScreen(
        onLogIn = { welcomeViewModel.handleLogIn() },
        onSignUp = { welcomeViewModel.handleSingUp() }
//        onSignInSignUp = { email ->
//            welcomeViewModel.handleSignInSignUp(
//                email = email,
//                onNavigateToSignIn = onNavigateToSignIn,
//                onNavigateToSignUp = onNavigateToSignUp
//            )
//        },

    )
}