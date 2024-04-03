package com.example.habicted_app.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.habicted_app.R
import com.example.habicted_app.utils.ValidateEmail


@Composable
fun EmailInputField(modifier: Modifier = Modifier) {
    //TODO: use viewModel if needed
    var emailInput by rememberSaveable { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    var hasBeenTouched by rememberSaveable { mutableStateOf(false) }

    fun isEmailValid(email: String): Boolean {
        return ValidateEmail().validate(email).successful
    }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                if (!hasBeenTouched && focusState.isFocused) {
                    hasBeenTouched = true
                }
            },
        value = emailInput,
        onValueChange = {
            emailInput = it
        },
        label = {
            Text(
                text = stringResource(id = R.string.email),
                style = MaterialTheme.typography.bodyMedium,
            )
        },

        textStyle = MaterialTheme.typography.bodyMedium,
        singleLine = true,
        isError = !isEmailValid(emailInput) && hasBeenTouched && !isFocused,
        supportingText = {
            if (!isEmailValid(emailInput) && hasBeenTouched && !isFocused) {
                ValidateEmail().validate(emailInput).errorMessage?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    )
}

@Composable
fun PasswordInputField(label: @Composable () -> Unit = {}) {
    //TODO: Implement viewModel
    var password by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = password,
        onValueChange = {
            password = it
        },
        label = label,
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyMedium,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        supportingText = {}
    )
}