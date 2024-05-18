package com.example.habicted_app.navigation.graphs

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.habicted_app.auth.model.AuthState
import com.example.habicted_app.auth.model.AuthViewModel
import com.example.habicted_app.navigation.routes.access.ForgotPasswordRoute
import com.example.habicted_app.navigation.routes.access.RegisterRoute
import com.example.habicted_app.navigation.routes.access.WelcomeRoute

fun NavGraphBuilder.AuthenticationGraph(
    navController: NavHostController,
) {
    navigation(
        startDestination = AuthRoute.Login.route,
        route = Graphs.AUTHENTICATION
    ) {
        composable(route = AuthRoute.Login.route) {
            val authViewModel: AuthViewModel = hiltViewModel()
            val authState by authViewModel.authState.collectAsState(initial = AuthState(isLoading = false))
            WelcomeRoute(
                onConfirmLogIn = {
                    navController.navigate(route = Graphs.HOME)
                },
                onNavigateToSignUp = {
                    navController.navigate(route = AuthRoute.Register.route)
                },
                onNavigateToForgotedPassword = {
                    navController.navigate(route = AuthRoute.Forgot.route)
                },
                tryLogin = { email, password ->
                    authViewModel.loginUser(email, password)
                },
                state = authState
            )
        }
        composable(route = AuthRoute.Register.route) {
            val authViewModel: AuthViewModel = hiltViewModel()
            val authState by authViewModel.authState.collectAsState(initial = AuthState(isLoading = false))
            when {
                authState.isSuccess?.isNotEmpty() == true -> {
                    navController.navigate(route = Graphs.HOME)
                }
            }
            RegisterRoute(
                onBack = {
                    navController.popBackStack()
                },
                signUpState = authState,
                onRegister = { email, password ->
                    authViewModel.registerUser(email, password)
                }
            )
        }
        composable(route = AuthRoute.Forgot.route) {
            val authViewModel: AuthViewModel = hiltViewModel()
            val authState by authViewModel.authState.collectAsState(initial = AuthState(isLoading = false))
            ForgotPasswordRoute(
                onBack = {
                    navController.popBackStack()
                },
                recoverPassword = { email ->
                    authViewModel.recoverPassword(email)
                },
                state = authState
            )
        }
    }


}

sealed class AuthRoute(val route: String) {
    object Login : AuthRoute(route = "LOGIN")
    object Register : AuthRoute(route = "SIGN_UP")
    object Forgot : AuthRoute(route = "FORGOT")
}