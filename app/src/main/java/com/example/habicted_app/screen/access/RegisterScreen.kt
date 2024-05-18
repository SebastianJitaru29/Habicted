package com.example.habicted_app.screen.access

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habicted_app.R
import com.example.habicted_app.auth.model.AuthState
import com.example.habicted_app.screen.EmailInputField
import com.example.habicted_app.screen.PasswordInputField
import com.example.habicted_app.ui.theme.righteousFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onRegister: (String, String) -> Unit = { _, _ -> },
    signUpState: AuthState,
) {
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name).uppercase(),
                        style = TextStyle(fontSize = 32.sp, fontFamily = righteousFamily),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->

        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Create an account",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                EmailInputField(
                    modifier = Modifier.fillMaxWidth(),
                    email = email,
                    onEmailChange = { email = it }
                )

                PasswordInputField(
                    modifier = Modifier.fillMaxWidth(),
                    password = password,
                    onPasswordChange = { password = it },
                    label = {
                        Text(
                            text = stringResource(id = R.string.password),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    isPasswordValid = {
                        when {
                            password.isEmpty() -> context.getString(R.string.password_cannot_be_empty)
                            password.length < 6 -> context.getString(R.string.password_must_be_at_least_6_characters_long)
                            else -> null
                        }
                    },
                )

                PasswordInputField(
                    modifier = Modifier.fillMaxWidth(),
                    password = confirmPassword,
                    onPasswordChange = { confirmPassword = it },
                    label = {
                        Text(
                            text = stringResource(id = R.string.confirm_password),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    isPasswordValid = {
                        when {
                            confirmPassword != password -> context.getString(R.string.passwords_do_not_match)
                            else -> null
                        }
                    },
                )



                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (signUpState.isLoading) {
                        CircularProgressIndicator()
                    }
                }
                LaunchedEffect(key1 = signUpState.isSuccess) {
                    scope.launch {
                        if (signUpState.isSuccess?.isNotEmpty() == true) {
                            val success = signUpState.isSuccess
                            Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                LaunchedEffect(key1 = signUpState.isError) {
                    scope.launch {
                        if (signUpState.isError?.isNotBlank() == true) {
                            val error = signUpState.isError
                            Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            Button(
                onClick = {
                    scope.launch {
                        onRegister(email, password)
                    }
                },
                shape = RoundedCornerShape(49.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(modifier = Modifier.padding(10.dp), text = "Create account")
            }
        }
    }
}


@Preview
@Composable
private fun RegisterPrev() {
    RegisterScreen(
        modifier = Modifier,
        onBack = {},
        onRegister = { _, _ ->
        },
        signUpState = AuthState(isSuccess = "Success")
    )
}


