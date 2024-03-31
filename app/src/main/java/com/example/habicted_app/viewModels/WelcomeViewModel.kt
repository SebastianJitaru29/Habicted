package com.example.habicted_app.viewModels

import androidx.lifecycle.ViewModel

class WelcomeViewModel() : ViewModel() {

    fun handleSignInSignUp(
        email: String,
        onNavigateToSignIn: (email: String) -> Unit,
        onNavigateToSignUp: (email: String) -> Unit,
    ) {
        val registeredEmail = false

        // if email is registered go to sign in else sign up
        if (registeredEmail) {
            onNavigateToSignIn(email)
        } else {
            onNavigateToSignUp(email)
        }
    }

    fun handleLogIn(
    ) {
    }

    fun handleSingUp(
    ) {

    }
}