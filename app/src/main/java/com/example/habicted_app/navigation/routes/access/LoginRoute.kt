package com.example.habicted_app.navigation.routes.access

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habicted_app.screen.access.WelcomeScreen
import com.example.habicted_app.viewModels.WelcomeViewModel

@Composable
fun WelcomeRoute(
    onConfirmLogIn: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotedPassword: () -> Unit,

) {
    val welcomeViewModel: WelcomeViewModel = viewModel()

    WelcomeScreen(
        onLogIn = {
            // If login ok then do:
            onConfirmLogIn()
        },
        onForgotedPassword = onNavigateToForgotedPassword
        ,
        onSignUp = onNavigateToSignUp
//        onSignInSignUp = { email ->
//            welcomeViewModel.handleSignInSignUp(
//                email = email,
//                onNavigateToSignIn = onNavigateToSignIn,
//                onNavigateToSignUp = onNavigateToSignUp
//            )
//        },

    )
}