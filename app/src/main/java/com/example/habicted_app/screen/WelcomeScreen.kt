package com.example.habicted_app.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habicted_app.R
import com.example.habicted_app.ui.theme.HabictedAppTheme
import com.example.habicted_app.ui.theme.righteousFamily

@Composable
fun WelcomeScreen(
    onLogIn: () -> Unit,
    onSignUp: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            //TODO: Add logo
            Text(
                text = stringResource(id = R.string.app_name).uppercase(),
                style = TextStyle(fontSize = 40.sp, fontFamily = righteousFamily),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            EmailInputField()
            PasswordInputField()
        }

        Spacer(modifier = Modifier.height(32.dp))

        ActionButtons(onLogIn = onLogIn, onSignUp = onSignUp)
    }

}

@Composable
fun ActionButtons(onLogIn: () -> Unit, onSignUp: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onLogIn,
            shape = RoundedCornerShape(49.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(49.dp)
        ) {
            Text(text = stringResource(id = R.string.login))
        }

        Text(
            text = stringResource(id = R.string.or),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.paddingFromBaseline(top = 24.dp),
        )

        OutlinedButton(
            onClick = onSignUp,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.signup))
        }
    }
}

@Composable
fun EmailInputField() {
    //TODO: Implement viewModel
    var email by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = email,
        onValueChange = {
            email = it
        },
        label = {
            Text(
                text = stringResource(id = R.string.email),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyMedium,
        singleLine = true
    )
}

@Composable
fun PasswordInputField() {
    //TODO: Implement viewModel
    var password by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = password,
        onValueChange = {
            password = it
        },
        label = {
            Text(
                text = stringResource(id = R.string.password),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyMedium,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true
    )
}


@Preview(name = "Welcome light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Welcome dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewEmailOutlinedTextField() {
    HabictedAppTheme {
        WelcomeScreen(onLogIn = { }, onSignUp = {})
    }
}

