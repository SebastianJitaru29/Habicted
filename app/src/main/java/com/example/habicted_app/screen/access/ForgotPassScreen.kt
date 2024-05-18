package com.example.habicted_app.screen.access

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.habicted_app.ui.theme.HabictedAppTheme
import com.example.habicted_app.ui.theme.righteousFamily
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    state: AuthState,
    recoverPassword: (String) -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
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
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(20.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            EmailInputField(
                modifier = Modifier.fillMaxWidth(),
                email = email,
                onEmailChange = { email = it }
            )

            Button(
                onClick = {
                    scope.launch {
                        recoverPassword(email)
                    }
                },
                shape = RoundedCornerShape(49.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(49.dp)
            ) {
                Text(
                    text = "Recover password",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                }
            }
            LaunchedEffect(key1 = state.isSuccess) {
                scope.launch {
                    if (state.isSuccess?.isNotEmpty() == true) {
                        val success = state.isSuccess
                        Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                    }
                }
            }
            LaunchedEffect(key1 = state.isError) {
                scope.launch {
                    if (state.isError?.isNotBlank() == true) {
                        val error = state.isError
                        Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}

@Preview(name = "Welcome dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview() {
    HabictedAppTheme {
        ForgotPasswordScreen(
            onBack = {},
            state = AuthState(isLoading = false),
            recoverPassword = {})
    }
}