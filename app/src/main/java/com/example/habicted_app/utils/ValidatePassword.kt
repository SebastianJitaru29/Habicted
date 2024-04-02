package com.example.habicted_app.utils

import android.util.Patterns
import java.util.regex.Pattern

class ValidatePassword {

    fun validate(password: String): ValidationResult {
        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "Password must have at least 8 characters"
            )
        }

        val containsDigitsAndLetters =
            password.any { it.isDigit() } && password.any { it.isLetter() }
        if (!containsDigitsAndLetters) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to contain at least one letter and digit"
            )
        }

        return ValidationResult(true)
    }

}