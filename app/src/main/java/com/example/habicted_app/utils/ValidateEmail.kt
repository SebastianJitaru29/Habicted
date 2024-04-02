package com.example.habicted_app.utils

import android.util.Patterns
import java.util.regex.Pattern

class ValidateEmail {

    fun validate(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The email can't be blank"
            )
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Not valid email"
            )
        }

        return ValidationResult(true)
    }

}