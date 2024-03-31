package com.example.habicted_app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habicted_app.Destinations.SIGN_IN_ROUTE
import com.example.habicted_app.Destinations.WELCOME_ROUTE
import com.example.habicted_app.routes.WelcomeRoute
import com.example.habicted_app.screen.LoginPage

object Destinations {
    const val WELCOME_ROUTE = "welcome"
    const val SIGN_UP_ROUTE = "signup/{email}"
    const val SIGN_IN_ROUTE = "signin/{email}"
    const val SURVEY_ROUTE = "survey"
    const val SURVEY_RESULTS_ROUTE = "surveyresults"
}

@Composable
fun HabiictedNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = WELCOME_ROUTE) {
        composable(route = WELCOME_ROUTE) {
            WelcomeRoute(
                onNavigateToSignIn = {
                    navController.navigate("signin/$it")
                },
                onNavigateToSignUp = {
                    navController.navigate("signup/$it")
                }
            )
        }

        composable(route = SIGN_IN_ROUTE) {
            val email = it.arguments?.getString("email")
        }
    }
}