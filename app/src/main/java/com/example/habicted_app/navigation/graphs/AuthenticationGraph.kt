package com.example.habicted_app.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.habicted_app.navigation.routes.access.ForgotPasswordRoute
import com.example.habicted_app.navigation.routes.access.RegisterRoute
import com.example.habicted_app.navigation.routes.access.WelcomeRoute

fun NavGraphBuilder.AuthenticationGraph(navController: NavHostController) {
    navigation(
        startDestination = AuthRoute.Login.route,
        route = Graphs.AUTHENTICATION
    ) {
        composable(route = AuthRoute.Login.route) {
            WelcomeRoute(
                onConfirmLogIn = {
                    navController.navigate(route = Graphs.HOME)
                },
                onNavigateToSignUp = {
                    navController.navigate(route = AuthRoute.Register.route)
                },
                onNavigateToForgotedPassword = {
                    navController.navigate(route = AuthRoute.Forgot.route)
                }
            )
        }
        composable(route = AuthRoute.Register.route) {
            RegisterRoute(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = AuthRoute.Forgot.route) {
            ForgotPasswordRoute(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }


}

sealed class AuthRoute(val route: String) {
    object Login : AuthRoute(route = "LOGIN")
    object Register : AuthRoute(route = "SIGN_UP")
    object Forgot : AuthRoute(route = "FORGOT")
}