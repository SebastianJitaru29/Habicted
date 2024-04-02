package com.example.habicted_app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.habicted_app.Destinations.HOME_ROUTE
import com.example.habicted_app.Destinations.LOG_IN_ROUTE
import com.example.habicted_app.Destinations.MAIN_ROUTE
import com.example.habicted_app.Destinations.REGISTER_ROUTE
import com.example.habicted_app.Destinations.WELCOME_ROUTE
import com.example.habicted_app.routes.WelcomeRoute
import com.example.habicted_app.screen.LoginPage

object Destinations {
    const val WELCOME_ROUTE = "welcome"
    const val LOG_IN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"
    const val MAIN_ROUTE = "main"
    const val HOME_ROUTE = "home"
}

@Composable
fun HabiictedNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = WELCOME_ROUTE) {
        navigation(startDestination = LOG_IN_ROUTE, route = WELCOME_ROUTE) {
            composable(route = LOG_IN_ROUTE) {
                WelcomeRoute(
                    onNavigateToLogIn = {
                        navController.navigate(MAIN_ROUTE) {
                            // when the user navigates to the MAIN_ROUTE, they won't be able to go back to the LOG_IN_ROUTE
                            popUpTo(WELCOME_ROUTE) { inclusive = true }
                        }
                    },
                    onNavigateToSignUp = {
                        navController.navigate(REGISTER_ROUTE)
                    }
                )
            }
            composable(route = REGISTER_ROUTE) {
                LoginPage(navController)
            }
        }
        navigation(startDestination = HOME_ROUTE, route = MAIN_ROUTE) {
            composable(route = HOME_ROUTE) {
                Greeting(name = "world")

            }
        }
    }
}