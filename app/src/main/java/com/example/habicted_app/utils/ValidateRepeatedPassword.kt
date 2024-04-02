package com.example.habicted_app.utils

import android.util.Patterns
import java.util.regex.Pattern

class ValidateRepeatedPassword {

    fun validate(password: String, repeatedPassword: String): ValidationResult {
        if (password != repeatedPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "The passwords don't match"
            )
        }

        return ValidationResult(true)
    }

}